package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.UserDTO;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.model.UserEntity;
import com.antilamer.thingTracker.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBO {

    private final UserRepo userRepo;

    @Autowired
    public UserBO(
            UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    public UserDTO getUser(Integer id) throws ValidationException {
        UserEntity userEntity = userRepo.findById(id).orElseThrow(() -> new ValidationException("There is no user with id: " + id));
        return new UserDTO(userEntity);
    }

}
