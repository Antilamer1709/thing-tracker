package com.antilamer.thingTracker.controller;

import com.antilamer.thingTracker.dto.GroupDTO;
import com.antilamer.thingTracker.dto.SelectGroupmateDTO;
import com.antilamer.thingTracker.dto.UserDTO;
import com.antilamer.thingTracker.exception.UnauthorizedException;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.service.GroupBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("api/group")
public class GroupController {

    private final GroupBO groupBO;

    @Autowired
    public GroupController(
            GroupBO expenseBO) {
        this.groupBO = expenseBO;
    }


    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping(value = "")
    public GroupDTO saveGroup(@RequestBody GroupDTO groupDTO) throws ValidationException, UnauthorizedException {
        log.debug("*** saveGroup() groupDTO: " + groupDTO);
        return groupBO.saveGroup(groupDTO);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping(value = "/search")
    public List<GroupDTO> searchUserGroups() {
        log.debug("*** searchUserGroups()");
        return groupBO.searchUserGroups();
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping(value = "{id}")
    public GroupDTO getUserGroup(@PathVariable Integer id) throws UnauthorizedException, ValidationException {
        log.debug("*** getUserGroup() id: " + id);
        return groupBO.getUserGroup(id);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping(value = "{groupId}/inviteToGroup")
    public void inviteToGroup(@PathVariable Integer groupId, @RequestBody List<UserDTO> users) throws UnauthorizedException, ValidationException {
        log.debug("*** inviteToGroup() users: " + users);
        log.debug("*** inviteToGroup() groupId: " + groupId);
        groupBO.inviteToGroup(groupId, users);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping(value = "/groupmates")
    public List<SelectGroupmateDTO> getGroupmates() {
        log.debug("*** getGroupmates()");
        return groupBO.getGroupmates();
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping(value = "{groupId}/kick/{userId}")
    public void kickUserFromGroup(@PathVariable Integer groupId, @PathVariable Integer userId) throws UnauthorizedException, ValidationException {
        log.debug("*** kickUserFromGroup() groupId: " + groupId);
        log.debug("*** inviteToGroup() userId: " + userId);
        groupBO.kickUserFromGroup(groupId, userId);
    }

}
