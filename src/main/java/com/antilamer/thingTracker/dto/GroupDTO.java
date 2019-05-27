package com.antilamer.thingTracker.dto;

import com.antilamer.thingTracker.model.GroupEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class GroupDTO {

    private Integer id;

    private String name;

    private UserDTO creator;

    private List<UserDTO> users;

    public GroupDTO(GroupEntity groupEntity) {
        this.id = groupEntity.getId();
        this.name = groupEntity.getName();
        this.creator = new UserDTO(groupEntity.getCreator());
        this.users = groupEntity.getUsers().stream().map(UserDTO::new).collect(Collectors.toList());
    }
}
