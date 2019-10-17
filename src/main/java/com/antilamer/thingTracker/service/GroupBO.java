package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.*;
import com.antilamer.thingTracker.exception.UnauthorizedException;
import com.antilamer.thingTracker.exception.ValidationException;

import java.util.List;

public interface GroupBO {

    GroupDTO saveGroup(GroupDTO groupDTO) throws ValidationException, UnauthorizedException;

    List<GroupDTO> searchUserGroups();

    GroupDTO getUserGroup(Integer id) throws ValidationException, UnauthorizedException;

    void inviteToGroup(Integer groupId, List<UserDTO> users) throws ValidationException, UnauthorizedException;

    List<SelectGroupmateDTO> getGroupmates();

    void kickUserFromGroup(Integer groupId, Integer userId) throws UnauthorizedException, ValidationException;

    void respondToInvite(ResponseToMessageDTO responseDTO) throws UnauthorizedException;

    List<MessageDTO> getGroupInvites();

}
