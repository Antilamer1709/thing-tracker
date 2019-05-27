package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.GroupDTO;
import com.antilamer.thingTracker.dto.MessageDTO;
import com.antilamer.thingTracker.dto.SelectGroupmateDTO;
import com.antilamer.thingTracker.dto.UserDTO;
import com.antilamer.thingTracker.exception.UnauthorizedException;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.model.GroupEntity;
import com.antilamer.thingTracker.model.UserEntity;
import com.antilamer.thingTracker.model.UserInviteEntity;
import com.antilamer.thingTracker.repository.GroupRepo;
import com.antilamer.thingTracker.repository.UserInviteRepo;
import com.antilamer.thingTracker.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GroupBOImpl implements GroupBO {

    private final GroupRepo groupRepo;
    private final UserRepo userRepo;
    private final UserInviteRepo userInviteRepo;
    private final AuthenticationBO authenticationBO;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public GroupBOImpl(
            GroupRepo groupRepo,
            UserRepo userRepo,
            UserInviteRepo userInviteRepo,
            AuthenticationBO authenticationBO,
            SimpMessagingTemplate simpMessagingTemplate) {
        this.groupRepo = groupRepo;
        this.userRepo = userRepo;
        this.userInviteRepo = userInviteRepo;
        this.authenticationBO = authenticationBO;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    @Override
    @Transactional
    public GroupDTO saveGroup(GroupDTO groupDTO) throws ValidationException, UnauthorizedException {
        GroupEntity groupEntity = getGroup(groupDTO);
        validateGroup(groupDTO, groupEntity);
        initGroupEntity(groupEntity, groupDTO);

        return new GroupDTO(groupRepo.save(groupEntity));
    }

    private void validateGroup(GroupDTO groupDTO, GroupEntity groupEntity) throws ValidationException, UnauthorizedException {
        if (groupDTO.getName() == null || groupDTO.getName().isEmpty()) {
            throw new ValidationException("Group name is empty!");
        }
        List<GroupEntity> entityList = groupRepo.findAllByName(groupDTO.getName());
        if (entityList.size() > 0 && entityList.stream().noneMatch(x -> x.getId().equals(groupEntity.getId()))) {
            throw new ValidationException("Group name already exists!");
        }

        if (groupEntity.getUser() != null) {
            authenticationBO.checkUserAccess(groupEntity.getUser());
        }
    }

    private GroupEntity getGroup(GroupDTO groupDTO) throws ValidationException {
        if (groupDTO.getId() != null) {
            return groupRepo.findById(groupDTO.getId())
                    .orElseThrow(() -> new ValidationException("There no such group with id: " + groupDTO.getId()));
        } else {
            return new GroupEntity();
        }
    }

    private void initGroupEntity(GroupEntity groupEntity, GroupDTO groupDTO) {
        groupEntity.setName(groupDTO.getName());
        if (groupEntity.getUser() == null) {
            groupEntity.setUser(authenticationBO.getLoggedUser());
        }
        if (groupEntity.getUsers() == null) {
            groupEntity.setUsers(new ArrayList<>());
        }
        if (!groupEntity.getUsers().contains(authenticationBO.getLoggedUser())) {
            groupEntity.getUsers().add(authenticationBO.getLoggedUser());
        }
    }


    @Override
    public List<GroupDTO> searchUserGroups() {
        return authenticationBO.getLoggedUser().getGroups().stream().map(GroupDTO::new).collect(Collectors.toList());
    }


    @Override
    public GroupDTO getUserGroup(Integer id) throws ValidationException, UnauthorizedException {
        GroupEntity groupEntity = groupRepo.findById(id)
                .orElseThrow(() -> new ValidationException("There no such group with id: " + id));
        authenticationBO.checkUserAccess(groupEntity.getUsers());

        return new GroupDTO(groupEntity);
    }


    @Override
    @Transactional
    public void inviteToGroup(Integer groupId, List<UserDTO> users) throws ValidationException, UnauthorizedException {
        List<UserInviteEntity> invites = new ArrayList<>();
        GroupEntity groupEntity = groupRepo.findById(groupId)
                .orElseThrow(() -> new ValidationException("There no such group with id: " + groupId));
        authenticationBO.checkUserAccess(groupEntity.getUsers());

        for (UserDTO user : users) {
            UserEntity userEntity = userRepo.findById(user.getId())
                    .orElseThrow(() -> new ValidationException("There no such user with id: " + user.getId()));

            if (!groupEntity.getUsers().contains(userEntity)) {
                UserInviteEntity inviteEntity = new UserInviteEntity();
                inviteEntity.setInviter(authenticationBO.getLoggedUser());
                inviteEntity.setTarget(userEntity);
                inviteEntity.setGroup(groupEntity);
                invites.add(userInviteRepo.save(inviteEntity));
            }
        }

        notifyUsers(invites);
    }

    private void notifyUsers(List<UserInviteEntity> invites) {
        invites.forEach(invite -> {
            simpMessagingTemplate.convertAndSend("/user-messages/" +
                    invite.getTarget().getId(), createInviteGroupMessage(invite));
        });
    }

    private MessageDTO createInviteGroupMessage(UserInviteEntity invite) {
        MessageDTO messageDTO = new MessageDTO();
        String username = authenticationBO.getLoggedUser().getFullName() + "(" + authenticationBO.getLoggedUser().getUsername() + ")";
        messageDTO.setMessage(username + " invited you to join group!");
        messageDTO.setId(invite.getId());
        return messageDTO;
    }

    @Override
    @Transactional
    public void acceptInvite(UserInviteEntity inviteEntity) {
        GroupEntity groupEntity = inviteEntity.getGroup();
        if (!groupEntity.getUsers().contains(inviteEntity.getTarget())) {
            groupEntity.getUsers().add(inviteEntity.getTarget());
        }
        groupRepo.save(groupEntity);
    }

    @Override
    @Transactional
    public List<SelectGroupmateDTO> getGroupmates() {
        List<SelectGroupmateDTO> result = new ArrayList<>();

        authenticationBO.getLoggedUser().getGroups().forEach(group -> {
            result.add(new SelectGroupmateDTO(group));
            group.getUsers().forEach(user -> result.add(new SelectGroupmateDTO(group, user)));
        });

        return result;
    }

    @Override
    @Transactional
    public void kickUserFromGroup(Integer groupId, Integer userId) throws UnauthorizedException, ValidationException {
        GroupEntity groupEntity = groupRepo.findById(groupId)
                .orElseThrow(() -> new ValidationException("There no such group with id: " + groupId));
        authenticationBO.checkUserAccess(groupEntity.getUser());

        boolean removed = groupEntity.getUsers().removeIf(x -> x.getId().equals(userId));
        if (!removed) {
            throw new ValidationException("There no such user in group with userId: " + userId);
        }

        groupRepo.save(groupEntity);
    }

}
