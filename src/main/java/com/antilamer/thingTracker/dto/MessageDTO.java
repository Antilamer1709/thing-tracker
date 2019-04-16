package com.antilamer.thingTracker.dto;

import com.antilamer.thingTracker.model.UserEntity;
import com.antilamer.thingTracker.model.UserInviteEntity;
import lombok.Data;

@Data
public class MessageDTO {

    private String message;

    private String sender;

    private String fromId;

    private String toId;


    public MessageDTO() {

    }

    public MessageDTO(UserInviteEntity inviteEntity) {
        UserEntity inviter = inviteEntity.getInviter();
        this.message = "Invited you to join group!";
        this.sender = inviter.getFullName() + "(" + inviter.getEmail() + ")";
    }

}
