package com.antilamer.thingTracker.dto;

import com.antilamer.thingTracker.model.UserEntity;
import com.antilamer.thingTracker.model.UserInviteEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageDTO {

    private Integer id;

    private String message;

    private String sender;

    private String fromId;

    private String toId;

    private Boolean loading;


    public MessageDTO(UserInviteEntity inviteEntity) {
        UserEntity inviter = inviteEntity.getInviter();
        this.id = inviteEntity.getId();
        this.message = "Invited you to join group!";
        this.sender = inviter.getFullName() + "(" + inviter.getEmail() + ")";
    }

    public MessageDTO(UserInviteEntity inviteEntity, String message) {
        this.id = inviteEntity.getId();
        this.message = message;
    }

}
