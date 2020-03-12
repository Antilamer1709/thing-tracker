package com.antilamer.thingTracker.dto;

import com.antilamer.thingTracker.enums.GroupmateType;
import com.antilamer.thingTracker.domain.GroupEntity;
import com.antilamer.thingTracker.domain.UserEntity;
import lombok.Data;

@Data
public class SelectGroupmateDTO {

    private Integer groupId;

    private Integer userId;

    private String label;

    private GroupmateType type;

    public SelectGroupmateDTO() {
    }

    public SelectGroupmateDTO(GroupEntity groupEntity) {
        this.groupId = groupEntity.getId();
        this.label = groupEntity.getName();
        this.type = GroupmateType.GROUP;
    }

    public SelectGroupmateDTO(GroupEntity groupEntity, UserEntity userEntity) {
        this.groupId = groupEntity.getId();
        this.userId = userEntity.getId();
        this.label = userEntity.getUsername();
        this.type = GroupmateType.USER;
    }
}
