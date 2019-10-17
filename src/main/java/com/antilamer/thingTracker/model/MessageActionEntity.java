package com.antilamer.thingTracker.model;

import com.antilamer.thingTracker.enums.MessageAction;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "message_action", schema = "thing_tracker")
public class MessageActionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "action")
    @Enumerated(EnumType.STRING)
    private MessageAction action;
}
