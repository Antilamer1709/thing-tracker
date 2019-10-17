package com.antilamer.thingTracker.repository;

import com.antilamer.thingTracker.enums.MessageAction;
import com.antilamer.thingTracker.model.MessageActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageActionRepo extends JpaRepository<MessageActionEntity, Integer> {

    MessageActionEntity findOneByAction(MessageAction action);

}
