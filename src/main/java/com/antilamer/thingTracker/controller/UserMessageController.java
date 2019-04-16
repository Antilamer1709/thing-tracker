package com.antilamer.thingTracker.controller;

import com.antilamer.thingTracker.dto.MessageDTO;
import com.antilamer.thingTracker.service.UserMessageBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/message")
public class UserMessageController {

    private final UserMessageBO userMessageBO;

    @Autowired
    public UserMessageController(UserMessageBO userMessageBO) {
        this.userMessageBO = userMessageBO;
    }


    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping()
    public List<MessageDTO> getUserMessages() {
        log.debug("*** getUserMessages()");
        return userMessageBO.getUserMessages();
    }

}
