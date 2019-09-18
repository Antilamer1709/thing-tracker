package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.JwtAuthenticationResponseDTO;
import com.antilamer.thingTracker.dto.RegistrationDTO;
import com.antilamer.thingTracker.dto.UserDTO;
import com.antilamer.thingTracker.enums.UserRole;
import com.antilamer.thingTracker.exception.UnauthorizedException;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.model.RoleEntity;
import com.antilamer.thingTracker.model.UserEntity;
import com.antilamer.thingTracker.repository.RoleRepo;
import com.antilamer.thingTracker.repository.UserRepo;
import com.antilamer.thingTracker.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationBOImpl implements AuthenticationBO {

    private final UserRepo userRepo;

    private final RoleRepo roleRepo;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;


    public UserEntity getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ANONYMOUS")) {
                return null;
            }
        }
        if (authentication.getPrincipal().getClass().equals(UserEntity.class)) {
            return (UserEntity) authentication.getPrincipal();
        } else {
            return createUserEntity((UserDetails) authentication.getPrincipal());
        }
    }

    private UserEntity createUserEntity(UserDetails principal) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(principal.getUsername());
        userEntity.setPassword(principal.getPassword());
        userEntity.setRoles(new ArrayList<>());

        for (GrantedAuthority authority : principal.getAuthorities()) {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setCode(authority.getAuthority());
            roleEntity.setName(authority.getAuthority());
            userEntity.getRoles().add(roleEntity);
        }

        return userEntity;
    }

    @Transactional
    public UserDTO getLoggedUserDTO() {
        UserEntity user = getLoggedUser();
        if (user != null) {
            UserDTO userDTO = new UserDTO(user);
            user.getRoles().forEach(x -> userDTO.getRoles().add(x.getCode()));
            log.debug("*** getLoggedUserDTO() userDTO: " + userDTO);
            return userDTO;
        } else {
            log.debug("*** getLoggedUserDTO() userDTO: anonymous");
            return null;
        }
    }

    @Transactional
    public void registerUser(RegistrationDTO registrationDTO) throws ValidationException {
        validateRegistration(registrationDTO);
        UserEntity user = new UserEntity();
        initUser(user, registrationDTO);
        addUserRoles(user, UserRole.USER);
        userRepo.save(user);
    }

    private void validateRegistration(RegistrationDTO registrationDTO) throws ValidationException {
        if (registrationDTO.hasNullFields() || registrationDTO.hasEmptyFields()) {
            throw new ValidationException("Registration object is not valid!");
        }
        UserEntity user = userRepo.findByEmailIgnoreCase(registrationDTO.getEmail());
        if (user != null) {
            throw new ValidationException("User with the same username is already registered!");
        }
    }

    private void initUser(UserEntity user, RegistrationDTO registrationDTO) {
        user.setFullName(registrationDTO.getFullName());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
    }

    @Transactional
    public void addUserRoles(UserEntity user, UserRole... userRoles) {
        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }

        for (UserRole userRole : userRoles) {
            RoleEntity roleEntity = roleRepo.findByCode(userRole.getValue());
            user.getRoles().add(roleEntity);
        }
    }

    @Transactional
    public void checkUserAccess(UserEntity user) throws UnauthorizedException {
        UserEntity loggedUser = getLoggedUser();
        if (user == null) {
            throw new UnauthorizedException("There is no such user in database!");
        }
        if (!loggedUser.getId().equals(user.getId())) {
            checkAdminAccess(loggedUser);
        }
    }

    @Transactional
    public void checkAdminAccess(UserEntity user) throws UnauthorizedException {
        boolean isAdmin = user.getRoles().stream().anyMatch(x -> x.getCode().equals(UserRole.ADMIN.getValue()));
        if (!isAdmin) {
            throw new UnauthorizedException("Permission denied!");
        }
    }

    @Transactional
    public void checkUserAccess(Collection<UserEntity> users) throws UnauthorizedException {
        UserEntity loggedUser = getLoggedUser();
        boolean userContains = users.stream().map(UserEntity::getId).anyMatch(x -> x.equals(loggedUser.getId()));
        if (!userContains) {
            checkAdminAccess(loggedUser);
        }
    }

    public JwtAuthenticationResponseDTO login(UserDTO userDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getEmail(),
                        userDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return new JwtAuthenticationResponseDTO(jwt);
    }

    public String getGitProperties() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("git.properties");
        try {
            return readFromInputStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return "{ \"error\" : \"Version information could not be retrieved\" }";
        }
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}