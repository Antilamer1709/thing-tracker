package com.antilamer.thingTracker.controller;

import com.antilamer.thingTracker.dto.UserDTO;
import com.antilamer.thingTracker.exception.UnauthorizedException;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;


    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping(value = "{id}")
    public UserDTO getUser(@PathVariable Integer id) throws ValidationException, UnauthorizedException {
        log.debug("*** getUser() id: " + id);
        return userService.getUser(id);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping(value = "/autocomplete")
    public List<UserDTO> searchUserSuggestions(@RequestParam String predicate) {
        log.debug("*** searchUserSuggestions() predicate: " + predicate);
        return userService.searchUserSuggestions(predicate);
    }

}
