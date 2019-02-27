package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.RegistrationDTO;
import com.antilamer.thingTracker.dto.UserDTO;
import com.antilamer.thingTracker.enums.UserRole;
import com.antilamer.thingTracker.exception.UnauthorizedException;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.model.RoleEntity;
import com.antilamer.thingTracker.model.UserEntity;
import com.antilamer.thingTracker.repository.RoleRepo;
import com.antilamer.thingTracker.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Slf4j
@Service
public class AuthenticationBO {

    private final UserRepo userRepo;

    private final RoleRepo roleRepo;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationBO(
            UserRepo userRepo,
            RoleRepo roleRepo,
            PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ANONYMOUS")) {
                return null;
            }
        }
        return (UserEntity) authentication.getPrincipal();
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
        UserEntity user = userRepo.findByUsernameIgnoreCase(registrationDTO.getUsername());
        if (user != null) {
            throw new ValidationException("User with the same username is already registered!");
        }
    }

    private void initUser(UserEntity user, RegistrationDTO registrationDTO) {
        user.setFirstName(registrationDTO.getFirstName());
        user.setLastName(registrationDTO.getLastName());
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
    }

    private void addUserRoles(UserEntity user, UserRole... userRoles) {
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
            boolean isAdmin = loggedUser.getRoles().stream().anyMatch(x -> x.getCode().equals(UserRole.ADMIN.getValue()));
            if (!isAdmin) {
                throw new UnauthorizedException("Permission denied!");
            }
        }
    }
}