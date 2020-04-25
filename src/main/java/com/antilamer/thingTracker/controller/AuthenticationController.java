package com.antilamer.thingTracker.controller;

import com.antilamer.thingTracker.dto.HostDTO;
import com.antilamer.thingTracker.dto.JwtAuthenticationResponseDTO;
import com.antilamer.thingTracker.dto.RegistrationDTO;
import com.antilamer.thingTracker.dto.UserDTO;
import com.antilamer.thingTracker.exception.ApplicationException;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @PostMapping(value = "/registration")
    @ResponseStatus(value = HttpStatus.OK)
    public void registration(@RequestBody RegistrationDTO registrationDTO) throws ValidationException {
        log.debug("*** registration() registrationDTO: " + registrationDTO);
        authenticationService.registerUser(registrationDTO);
    }

    @PostMapping(value = "/loggedUser")
    public UserDTO loggedUser() {
        log.debug("*** loggedUser()");
        return authenticationService.getLoggedUserDTO();
    }

    @PostMapping("/login")
    public JwtAuthenticationResponseDTO login(@RequestBody UserDTO userDTO) {
        log.debug("*** login() userDTO:" + userDTO);
        return authenticationService.login(userDTO);
    }

    @GetMapping(value = "/hostInfo")
    public HostDTO getHostInfo() throws ApplicationException {
        log.debug("*** getHostInfo()");
        return authenticationService.getHostInfo();
    }

    @GetMapping(value = "/version")
    public String getGitProperties() {
        log.debug("*** getGitProperties()");
        return authenticationService.getGitProperties();
    }

}
