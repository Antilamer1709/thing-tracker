package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.MessageDTO;
import com.antilamer.thingTracker.dto.ResponseToMessageDTO;
import com.antilamer.thingTracker.exception.UnauthorizedException;
import com.antilamer.thingTracker.model.UserEntity;
import com.antilamer.thingTracker.model.UserInviteEntity;
import com.antilamer.thingTracker.repository.UserInviteRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMessageBOImpl implements UserMessageBO {

    private final UserInviteRepo userInviteRepo;
    private final AuthenticationBO authenticationBO;
    private final GroupBO groupBO;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @Override
    public List<MessageDTO> getUserMessages() {
        UserEntity loggedUser = authenticationBO.getLoggedUser();
        return userInviteRepo.findAllByTarget(loggedUser).stream().map(MessageDTO::new).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void respondToMessage(ResponseToMessageDTO responseDTO) throws UnauthorizedException {
        UserInviteEntity inviteEntity = userInviteRepo.findById(responseDTO.getMessageId())
                .orElseThrow(() -> new ValidationException("There no such message with id " + responseDTO.getMessageId()));
        authenticationBO.checkUserAccess(inviteEntity.getGroup().getUsers());
        if (responseDTO.getResponse()) {
            groupBO.acceptInvite(inviteEntity);
        }
        userInviteRepo.delete(inviteEntity);
        notifyInviteCreator(responseDTO.getResponse(), inviteEntity);
    }

    private void notifyInviteCreator(Boolean accepted, UserInviteEntity inviteEntity) {
        simpMessagingTemplate.convertAndSend("/topic/" +
                inviteEntity.getInviter().getId(), createInviteCreatorMessage(accepted, inviteEntity));
    }

    private MessageDTO createInviteCreatorMessage(Boolean accepted, UserInviteEntity inviteEntity) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(inviteEntity.getTarget().getFullName());
        stringBuilder.append("(");
        stringBuilder.append(inviteEntity.getTarget().getUsername());
        stringBuilder.append(")");
        stringBuilder.append(" has ");
        if (!accepted) {
            stringBuilder.append(" not ");
        }
        stringBuilder.append(" accepted your invitation to join group ");
        stringBuilder.append(inviteEntity.getGroup().getName());
        stringBuilder.append("!");

        return new MessageDTO(inviteEntity, stringBuilder.toString());
    }
}
