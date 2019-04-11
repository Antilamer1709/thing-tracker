package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.GroupDTO;
import com.antilamer.thingTracker.exception.UnauthorizedException;
import com.antilamer.thingTracker.exception.ValidationException;

import java.util.List;

public interface GroupBO {

    GroupDTO saveGroup(GroupDTO groupDTO) throws ValidationException, UnauthorizedException;

    List<GroupDTO> searchUserGroups();

    GroupDTO getUserGroup(Integer id) throws ValidationException, UnauthorizedException;
}
