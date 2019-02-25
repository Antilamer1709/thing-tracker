package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AuthenticationBO {

    @Transactional
    public UserDTO getLoggedUserDTO() {
        //todo implement
        return new UserDTO();
    }

}
