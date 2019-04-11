package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.GroupDTO;
import com.antilamer.thingTracker.exception.UnauthorizedException;
import com.antilamer.thingTracker.exception.ValidationException;

public interface GroupBO {

    GroupDTO saveGroup(GroupDTO groupDTO) throws ValidationException, UnauthorizedException;

}
