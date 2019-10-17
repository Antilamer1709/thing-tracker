package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.*;
import com.antilamer.thingTracker.enums.MessageAction;
import com.antilamer.thingTracker.exception.UnauthorizedException;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.model.GroupEntity;
import com.antilamer.thingTracker.model.UserEntity;
import com.antilamer.thingTracker.model.UserInviteEntity;
import com.antilamer.thingTracker.repository.GroupRepo;
import com.antilamer.thingTracker.repository.UserInviteRepo;
import com.antilamer.thingTracker.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupBOImpl implements GroupBO {

    private final GroupRepo groupRepo;
    private final UserRepo userRepo;
    private final UserInviteRepo userInviteRepo;
    private final AuthenticationBO authenticationBO;
    private final UserMessageBO userMessageBO;


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

        if (groupEntity.getCreator() != null) {
            authenticationBO.checkUserAccess(groupEntity.getCreator());
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
        if (groupEntity.getCreator() == null) {
            groupEntity.setCreator(authenticationBO.getLoggedUser());
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
                Optional<UserInviteEntity> inviteOpt = userInviteRepo.findAllByInviterAndTarget(authenticationBO.getLoggedUser(), userEntity);
                if (!inviteOpt.isPresent()) {
                    UserInviteEntity inviteEntity = new UserInviteEntity();
                    inviteEntity.setInviter(authenticationBO.getLoggedUser());
                    inviteEntity.setTarget(userEntity);
                    inviteEntity.setGroup(groupEntity);
                    invites.add(userInviteRepo.save(inviteEntity));
                    userMessageBO.notifyUser(userEntity, createInviteGroupMessage(inviteEntity));
                }
            }
        }
    }

    private MessageDTO createInviteGroupMessage(UserInviteEntity invite) {
        MessageDTO messageDTO = new MessageDTO();
        String username = authenticationBO.getLoggedUser().getFullName() + "(" + authenticationBO.getLoggedUser().getUsername() + ")";
        messageDTO.setMessage(username + " invited you to join group!");
        messageDTO.setId(invite.getId());
        messageDTO.setActions(new ArrayList<>(Arrays.asList(MessageAction.ACCEPT, MessageAction.REJECT)));
        return messageDTO;
    }

    @Override
    @Transactional
    public void respondToInvite(ResponseToMessageDTO responseDTO) throws UnauthorizedException {
        UserInviteEntity inviteEntity = userInviteRepo.findById(responseDTO.getMessageId())
                .orElseThrow(() -> new javax.validation.ValidationException("There no such message with id " + responseDTO.getMessageId()));
        authenticationBO.checkUserAccess(inviteEntity.getTarget());
        if (responseDTO.getResponse()) {
            acceptInvite(inviteEntity);
        }
        userInviteRepo.delete(inviteEntity);
        val messageDTO = createInviteCreatorMessage(responseDTO.getResponse(), inviteEntity);
        userMessageBO.addUserMessage(inviteEntity.getInviter(), messageDTO, MessageAction.READ);
    }

    private void acceptInvite(UserInviteEntity inviteEntity) {
        GroupEntity groupEntity = inviteEntity.getGroup();
        if (!groupEntity.getUsers().contains(inviteEntity.getTarget())) {
            groupEntity.getUsers().add(inviteEntity.getTarget());
        }
        groupRepo.save(groupEntity);
    }

    private MessageDTO createInviteCreatorMessage(Boolean accepted, UserInviteEntity inviteEntity) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(inviteEntity.getTarget().getFullName());
        stringBuilder.append("(");
        stringBuilder.append(inviteEntity.getTarget().getUsername());
        stringBuilder.append(")");
        stringBuilder.append(" has ");
        if (!accepted) {
            stringBuilder.append(" not ");
        }
        stringBuilder.append(" accepted your invitation to join group ");
        stringBuilder.append(inviteEntity.getGroup().getName());
        stringBuilder.append("!");

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessage(stringBuilder.toString());

        messageDTO.setActions(Collections.singletonList(MessageAction.READ));

        return messageDTO;
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
        // User can kick himself
        if (!authenticationBO.getLoggedUser().getId().equals(userId)) {
            authenticationBO.checkUserAccess(groupEntity.getCreator());
        }

        boolean removed = groupEntity.getUsers().removeIf(x -> x.getId().equals(userId));
        if (!removed) {
            throw new ValidationException("There no such user in group with userId: " + userId);
        }
        if (groupEntity.getCreator().getId().equals(userId)) {
            Optional<UserEntity> newCreatorOpt = groupEntity.getUsers().stream()
                    .filter(x -> !x.getId().equals(userId))
                    .findFirst();
            if (newCreatorOpt.isPresent()) {
                groupEntity.setCreator(newCreatorOpt.get());
                groupRepo.save(groupEntity);
            } else {
                groupRepo.delete(groupEntity);
            }
        }

    }

    @Override
    public List<MessageDTO> getGroupInvites() {
        UserEntity loggedUser = authenticationBO.getLoggedUser();
        return userInviteRepo.findAllByTarget(loggedUser).stream().map(MessageDTO::new).collect(Collectors.toList());
    }

}
