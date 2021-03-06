package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.GroupDTO;
import com.antilamer.thingTracker.dto.MessageDTO;
import com.antilamer.thingTracker.dto.SelectGroupmateDTO;
import com.antilamer.thingTracker.dto.UserDTO;
import com.antilamer.thingTracker.exception.UnauthorizedException;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.domain.GroupEntity;
import com.antilamer.thingTracker.domain.UserEntity;
import com.antilamer.thingTracker.domain.UserInviteEntity;
import com.antilamer.thingTracker.repository.GroupRepo;
import com.antilamer.thingTracker.repository.UserInviteRepo;
import com.antilamer.thingTracker.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepo groupRepo;
    private final UserRepo userRepo;
    private final UserInviteRepo userInviteRepo;
    private final AuthenticationService authenticationService;
    private final SimpMessagingTemplate simpMessagingTemplate;


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
            authenticationService.checkUserAccess(groupEntity.getCreator());
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
            groupEntity.setCreator(authenticationService.getLoggedUser());
        }
        if (groupEntity.getUsers() == null) {
            groupEntity.setUsers(new ArrayList<>());
        }
        if (!groupEntity.getUsers().contains(authenticationService.getLoggedUser())) {
            groupEntity.getUsers().add(authenticationService.getLoggedUser());
        }
    }


    @Override
    public List<GroupDTO> searchUserGroups() {
        return authenticationService.getLoggedUser().getGroups().stream().map(GroupDTO::new).collect(Collectors.toList());
    }


    @Override
    public GroupDTO getUserGroup(Integer id) throws ValidationException, UnauthorizedException {
        GroupEntity groupEntity = groupRepo.findById(id)
                .orElseThrow(() -> new ValidationException("There no such group with id: " + id));
        authenticationService.checkUserAccess(groupEntity.getUsers());

        return new GroupDTO(groupEntity);
    }


    @Override
    @Transactional
    public void inviteToGroup(Integer groupId, List<UserDTO> users) throws ValidationException, UnauthorizedException {
        List<UserInviteEntity> invites = new ArrayList<>();
        GroupEntity groupEntity = groupRepo.findById(groupId)
                .orElseThrow(() -> new ValidationException("There no such group with id: " + groupId));
        authenticationService.checkUserAccess(groupEntity.getUsers());

        for (UserDTO user : users) {
            UserEntity userEntity = userRepo.findById(user.getId())
                    .orElseThrow(() -> new ValidationException("There no such user with id: " + user.getId()));

            if (!groupEntity.getUsers().contains(userEntity)) {
                Optional<UserInviteEntity> inviteOpt = userInviteRepo.findAllByInviterAndTarget(authenticationService.getLoggedUser(), userEntity);
                if (!inviteOpt.isPresent()) {
                    UserInviteEntity inviteEntity = new UserInviteEntity();
                    inviteEntity.setInviter(authenticationService.getLoggedUser());
                    inviteEntity.setTarget(userEntity);
                    inviteEntity.setGroup(groupEntity);
                    invites.add(userInviteRepo.save(inviteEntity));
                }
            }
        }

        notifyUsers(invites);
    }

    private void notifyUsers(List<UserInviteEntity> invites) {
        invites.forEach(invite -> {
            simpMessagingTemplate.convertAndSend("/topic/" +
                    invite.getTarget().getId(), createInviteGroupMessage(invite));
        });
    }

    private MessageDTO createInviteGroupMessage(UserInviteEntity invite) {
        MessageDTO messageDTO = new MessageDTO();
        String username = authenticationService.getLoggedUser().getFullName() + "(" + authenticationService.getLoggedUser().getUsername() + ")";
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

        authenticationService.getLoggedUser().getGroups().forEach(group -> {
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
        if (!authenticationService.getLoggedUser().getId().equals(userId)) {
            authenticationService.checkUserAccess(groupEntity.getCreator());
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

}
