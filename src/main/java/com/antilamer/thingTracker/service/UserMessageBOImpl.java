package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.MessageDTO;
import com.antilamer.thingTracker.model.UserEntity;
import com.antilamer.thingTracker.repository.UserInviteRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserMessageBOImpl implements UserMessageBO {

    private final UserInviteRepo userInviteRepo;
    private final AuthenticationBO authenticationBO;

    @Autowired
    public UserMessageBOImpl(
            UserInviteRepo userInviteRepo,
            AuthenticationBO authenticationBO) {
        this.userInviteRepo = userInviteRepo;
        this.authenticationBO = authenticationBO;
    }


    @Override
    public List<MessageDTO> getUserMessages() {
        UserEntity loggedUser = authenticationBO.getLoggedUser();
        return userInviteRepo.findAllByTarget(loggedUser).stream().map(MessageDTO::new).collect(Collectors.toList());
    }
}
