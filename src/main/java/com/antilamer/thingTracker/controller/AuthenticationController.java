package com.antilamer.thingTracker.controller;

import com.antilamer.thingTracker.dto.RegistrationDTO;
import com.antilamer.thingTracker.dto.UserDTO;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.service.AuthenticationBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationBO authenticationBO;

    @PostMapping(value = "/registration")
    @ResponseStatus(value = HttpStatus.OK)
    public void registration(@RequestBody RegistrationDTO registrationDTO) throws ValidationException {
        log.debug("*** registration() registrationDTO: " + registrationDTO);
        authenticationBO.registerUser(registrationDTO);
    }

    @PostMapping(value = "/loggedUser")
    public UserDTO loggedUser() {
        log.debug("*** loggedUser()");
        return authenticationBO.getLoggedUserDTO();
    }

}
