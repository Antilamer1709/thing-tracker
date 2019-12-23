package com.antilamer.thingTracker.controller;

import com.antilamer.thingTracker.dto.MessageDTO;
import com.antilamer.thingTracker.service.UserMessageBO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/message")
public class UserMessageController {

    private final UserMessageBO userMessageBO;


    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping()
    public List<MessageDTO> getUserMessages() {
        log.debug("*** getUserMessages()");
        return userMessageBO.getUserMessages();
    }

}
