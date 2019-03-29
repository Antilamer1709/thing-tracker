package com.antilamer.thingTracker.controller;

import com.antilamer.thingTracker.dto.UserDTO;
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
    @GetMapping(value = "/autocomplete")
    public List<UserDTO> searchUserSuggestions(@RequestParam String predicate) {
        log.debug("*** searchUserSuggestions() predicate: " + predicate);
        return userBO.searchUserSuggestions(predicate);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping(value = "/addToGroup")
    public void addToGroup(@RequestBody List<UserDTO> users) {
        log.debug("*** addToGroup() users: " + users);
        userBO.addToGroup(users);
    }

}
