package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.MessageDTO;
import com.antilamer.thingTracker.enums.MessageAction;
import com.antilamer.thingTracker.model.UserEntity;
import com.antilamer.thingTracker.model.UserMessageEntity;
import com.antilamer.thingTracker.repository.UserMessageRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMessageBOImpl implements UserMessageBO {

    private final AuthenticationBO authenticationBO;
    private final UserMessageRepo userMessageRepo;
    private final CacheableDataBO cacheableDataBO;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @Override
    public List<MessageDTO> getUserMessages() {
        UserEntity loggedUser = authenticationBO.getLoggedUser();
        return userMessageRepo.findAllByUser(loggedUser).stream().map(MessageDTO::new).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addUserMessage(UserEntity user, MessageDTO messageDTO, MessageAction... actions) {
        UserMessageEntity userMessageEntity = createUserMessageEntity(user, messageDTO, actions);
        userMessageRepo.save(userMessageEntity);
        simpMessagingTemplate.convertAndSend("/topic/" + user.getId(), messageDTO);
    }

    @Override
    public void notifyUser(UserEntity user, MessageDTO messageDTO) {
        simpMessagingTemplate.convertAndSend("/topic/" + user.getId(), messageDTO);
    }

    private UserMessageEntity createUserMessageEntity(UserEntity user, MessageDTO messageDTO, MessageAction[] actions) {
        UserMessageEntity userMessageEntity = new UserMessageEntity(messageDTO.getMessage(), user);
        userMessageEntity.setDate(LocalDateTime.now());
        userMessageEntity.setActions(new ArrayList<>());

        for (MessageAction action : actions) {
            userMessageEntity.getActions().add(cacheableDataBO.findMessageAction(action));
        }
        return userMessageEntity;
    }
}
