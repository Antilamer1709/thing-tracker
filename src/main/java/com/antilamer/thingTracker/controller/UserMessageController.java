package com.antilamer.thingTracker.controller;

import com.antilamer.thingTracker.dto.MessageDTO;
import com.antilamer.thingTracker.dto.ResponseToMessageDTO;
import com.antilamer.thingTracker.exception.UnauthorizedException;
import com.antilamer.thingTracker.service.UserMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/message")
public class UserMessageController {

    private final UserMessageService userMessageService;


    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping()
    public List<MessageDTO> getUserMessages() {
        log.debug("*** getUserMessages()");
        return userMessageService.getUserMessages();
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping(value = "/respond")
    @ResponseStatus(value = HttpStatus.OK)
    public void respondToMessage(@RequestBody @Valid ResponseToMessageDTO responseDTO) throws UnauthorizedException {
        log.debug("*** respondToMessage() responseDTO: " + responseDTO);
        userMessageService.respondToMessage(responseDTO);
    }

}
