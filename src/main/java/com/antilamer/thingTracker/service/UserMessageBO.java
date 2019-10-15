package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.MessageDTO;
import com.antilamer.thingTracker.dto.ResponseToMessageDTO;
import com.antilamer.thingTracker.exception.UnauthorizedException;

import java.util.List;

public interface UserMessageBO {

    List<MessageDTO> getUserMessages();

    void respondToMessage(ResponseToMessageDTO responseDTO) throws UnauthorizedException;
}
