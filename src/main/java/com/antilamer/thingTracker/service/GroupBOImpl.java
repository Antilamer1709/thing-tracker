package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.GroupDTO;
import com.antilamer.thingTracker.exception.UnauthorizedException;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.model.GroupEntity;
import com.antilamer.thingTracker.repository.GroupRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GroupBOImpl implements GroupBO {

    private final GroupRepo groupRepo;
    private final AuthenticationBO authenticationBO;

    @Autowired
    public GroupBOImpl(
            GroupRepo groupRepo,
            AuthenticationBO authenticationBO) {
        this.groupRepo = groupRepo;
        this.authenticationBO = authenticationBO;
    }


    @Override
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
                    .orElseThrow(() -> new ValidationException("There no such group with id " + groupDTO.getId()));
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
}
