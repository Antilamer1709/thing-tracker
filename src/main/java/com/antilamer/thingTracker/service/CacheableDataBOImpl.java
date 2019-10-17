package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.enums.MessageAction;
import com.antilamer.thingTracker.model.MessageActionEntity;
import com.antilamer.thingTracker.repository.MessageActionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheableDataBOImpl implements CacheableDataBO {

    private final MessageActionRepo messageActionRepo;


    @Override
    @Cacheable("message_action")
    public MessageActionEntity findMessageAction(MessageAction action) {
        return messageActionRepo.findOneByAction(action);
    }

}
