package com.antilamer.thingTracker.dto;

import com.antilamer.thingTracker.enums.MessageAction;
import com.antilamer.thingTracker.model.MessageActionEntity;
import com.antilamer.thingTracker.model.UserEntity;
import com.antilamer.thingTracker.model.UserInviteEntity;
import com.antilamer.thingTracker.model.UserMessageEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class MessageDTO {

    private Integer id;

    @NonNull
    private String message;

    private String sender;

    private String fromId;

    private String toId;

    private Boolean loading;

    private List<MessageAction> actions;

    public MessageDTO(UserInviteEntity inviteEntity) {
        UserEntity inviter = inviteEntity.getInviter();
        this.id = inviteEntity.getId();
        this.message = "Invited you to join group!";
        this.sender = inviter.getFullName() + "(" + inviter.getEmail() + ")";
    }

    public MessageDTO(UserMessageEntity messageEntity) {
        this.id = messageEntity.getId();
        this.message = messageEntity.getMessage();
        this.actions = messageEntity.getActions().stream().map(MessageActionEntity::getAction).collect(Collectors.toList());
    }

}
