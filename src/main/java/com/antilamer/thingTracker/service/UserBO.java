package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.UserDTO;
import com.antilamer.thingTracker.exception.UnauthorizedException;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.model.UserEntity;
import com.antilamer.thingTracker.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserBO {

    private final UserRepo userRepo;
    private final AuthenticationBO authenticationBO;

    @Autowired
    public UserBO(
            UserRepo userRepo,
            AuthenticationBO authenticationBO) {
        this.userRepo = userRepo;
        this.authenticationBO = authenticationBO;
    }


    public UserDTO getUser(Integer id) throws ValidationException, UnauthorizedException {
        UserEntity userEntity = userRepo.findById(id).orElseThrow(() -> new ValidationException("There is no user with id: " + id));
        checkUserAuthorization(userEntity);

        UserDTO userDTO = new UserDTO(userEntity);
        userEntity.getRoles().forEach(x -> userDTO.getRoles().add(x.getCode()));
        return userDTO;
    }

    private void checkUserAuthorization(UserEntity userEntity) throws UnauthorizedException {
        UserEntity loggedUser = authenticationBO.getLoggedUser();
        boolean authorized = loggedUser.getGroups()
                .stream().anyMatch(group -> group.getUsers()
                        .stream().anyMatch(user -> user.getId().equals(userEntity.getId())));
        if (!authorized) {
            throw new UnauthorizedException("User does not belong to any of your groups!");
        }
    }

    public List<UserDTO> searchUserSuggestions(String predicate) {
        return userRepo.findTop5ByFullNameOrUsername(PageRequest.of(0,5), predicate).stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

}
