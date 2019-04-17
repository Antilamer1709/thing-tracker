package com.antilamer.thingTracker.dto;

import com.antilamer.thingTracker.model.UserEntity;
import com.antilamer.thingTracker.model.UserInviteEntity;
import lombok.Data;

@Data
public class MessageDTO {

    private Integer id;

    private String message;

    private String sender;

    private String fromId;

    private String toId;


    public MessageDTO() {

    }

    public MessageDTO(UserInviteEntity inviteEntity) {
        UserEntity inviter = inviteEntity.getInviter();
        this.id = inviteEntity.getId();
        this.message = "Invited you to join group!";
        this.sender = inviter.getFullName() + "(" + inviter.getEmail() + ")";
    }

}
