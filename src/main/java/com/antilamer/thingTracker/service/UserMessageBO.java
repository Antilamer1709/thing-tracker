package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.MessageDTO;
import com.antilamer.thingTracker.dto.ResponseToMessageDTO;

import java.util.List;

public interface UserMessageBO {

    List<MessageDTO> getUserMessages();

    void respondToMessage(ResponseToMessageDTO responseDTO);
}
