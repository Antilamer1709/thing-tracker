package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.GroupDTO;
import com.antilamer.thingTracker.dto.SelectGroupmateDTO;
import com.antilamer.thingTracker.dto.UserDTO;
import com.antilamer.thingTracker.exception.UnauthorizedException;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.model.UserInviteEntity;

import java.util.List;

public interface GroupBO {

    GroupDTO saveGroup(GroupDTO groupDTO) throws ValidationException, UnauthorizedException;

    List<GroupDTO> searchUserGroups();

    GroupDTO getUserGroup(Integer id) throws ValidationException, UnauthorizedException;

    void inviteToGroup(Integer groupId, List<UserDTO> users) throws ValidationException, UnauthorizedException;

    void acceptInvite(UserInviteEntity inviteEntity);

    List<SelectGroupmateDTO> getGroupmates();

    void kickUserFromGroup(Integer groupId, Integer userId) throws UnauthorizedException, ValidationException;
}
