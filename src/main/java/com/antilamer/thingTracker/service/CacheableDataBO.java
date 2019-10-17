package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.enums.MessageAction;
import com.antilamer.thingTracker.model.MessageActionEntity;

public interface CacheableDataBO {

    MessageActionEntity findMessageAction(MessageAction action);

}
