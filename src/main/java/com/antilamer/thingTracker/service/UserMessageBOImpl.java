package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.MessageDTO;
import com.antilamer.thingTracker.dto.ResponseToMessageDTO;
import com.antilamer.thingTracker.model.UserEntity;
import com.antilamer.thingTracker.model.UserInviteEntity;
import com.antilamer.thingTracker.repository.UserInviteRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserMessageBOImpl implements UserMessageBO {

    private final UserInviteRepo userInviteRepo;
    private final AuthenticationBO authenticationBO;
    private final GroupBO groupBO;

    @Autowired
    public UserMessageBOImpl(
            UserInviteRepo userInviteRepo,
            AuthenticationBO authenticationBO,
            GroupBO groupBO) {
        this.userInviteRepo = userInviteRepo;
        this.authenticationBO = authenticationBO;
        this.groupBO = groupBO;
    }


    @Override
    public List<MessageDTO> getUserMessages() {
        UserEntity loggedUser = authenticationBO.getLoggedUser();
        return userInviteRepo.findAllByTarget(loggedUser).stream().map(MessageDTO::new).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void respondToMessage(ResponseToMessageDTO responseDTO) {
        UserInviteEntity inviteEntity = userInviteRepo.findById(responseDTO.getMessageId())
                .orElseThrow(() -> new ValidationException("There no such message with id " + responseDTO.getMessageId()));
        if (responseDTO.getResponse()) {
            groupBO.acceptInvite(inviteEntity);
        }
        userInviteRepo.delete(inviteEntity);
    }
}