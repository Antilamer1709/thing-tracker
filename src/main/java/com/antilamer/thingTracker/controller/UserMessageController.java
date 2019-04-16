package com.antilamer.thingTracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/socket")
@CrossOrigin("*")
public class UserMessageController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @MessageMapping("/send/message")
    public Map<String, String> useSocketCommunication(String message){
        //todo
        return null;
    }

}
