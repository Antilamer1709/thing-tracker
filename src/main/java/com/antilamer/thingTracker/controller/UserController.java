package com.antilamer.thingTracker.controller;

import com.antilamer.thingTracker.dto.UserDTO;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.service.UserBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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


    @GetMapping(value = "/getUser/{id}")
    public UserDTO getUser(@PathVariable Integer id) throws ValidationException {
        log.debug("*** getUser() id: " + id);
        return userBO.getUser(id);
    }

}
