package com.antilamer.thingTracker.controller;

import com.antilamer.thingTracker.dto.UserDTO;
import com.antilamer.thingTracker.exception.UnauthorizedException;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.service.UserBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserBO userBO;

    @Autowired
    public UserController(
            UserBO userBO) {
        this.userBO = userBO;
    }


    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping(value = "{id}")
    public UserDTO getUser(@PathVariable Integer id) throws ValidationException, UnauthorizedException {
        log.debug("*** getUser() id: " + id);
        return userBO.getUser(id);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping(value = "/autocomplete")
    public List<UserDTO> searchUserSuggestions(@RequestParam String predicate) {
        log.debug("*** searchUserSuggestions() predicate: " + predicate);
        return userBO.searchUserSuggestions(predicate);
    }

}
