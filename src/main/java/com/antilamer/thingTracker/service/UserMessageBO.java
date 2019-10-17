package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.MessageDTO;
import com.antilamer.thingTracker.enums.MessageAction;
import com.antilamer.thingTracker.model.UserEntity;

import java.util.List;

public interface UserMessageBO {

    List<MessageDTO> getUserMessages();

    void addUserMessage(UserEntity user, MessageDTO messageDTO, MessageAction... actions);

    void notifyUser(UserEntity user, MessageDTO messageDTO);

}
