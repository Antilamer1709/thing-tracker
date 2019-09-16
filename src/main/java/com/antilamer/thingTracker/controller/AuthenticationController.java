package com.antilamer.thingTracker.controller;

import com.antilamer.thingTracker.dto.HostDTO;
import com.antilamer.thingTracker.dto.JwtAuthenticationResponseDTO;
import com.antilamer.thingTracker.dto.RegistrationDTO;
import com.antilamer.thingTracker.dto.UserDTO;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.service.AuthenticationBO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/authentication")
public class AuthenticationController {

    private final AuthenticationBO authenticationBO;


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

    @PostMapping("/login")
    public JwtAuthenticationResponseDTO login(@RequestBody UserDTO userDTO) {
        log.debug("*** login() userDTO:" + userDTO);
        return authenticationBO.login(userDTO);
    }

    @GetMapping(value = "/hostName")
    public HostDTO getHostName() {
        log.debug("*** getHostName()");
        HostDTO hostDTO;

        try {
            InetAddress ip = InetAddress.getLocalHost();
            hostDTO = new HostDTO(ip);
            log.debug("*** Your current IP address : " + ip);
            log.debug("*** Your current Hostname : " + ip.getHostName());
        } catch (Exception e) {
            e.printStackTrace();
            hostDTO = new HostDTO();
        }

        return hostDTO;
    }

    @GetMapping(value = "/version")
    public String versionInformation() {
        return authenticationBO.getGitProperties();
    }

}
