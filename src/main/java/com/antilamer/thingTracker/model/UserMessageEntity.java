package com.antilamer.thingTracker.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "user_message", schema = "thing_tracker")
@NoArgsConstructor
@RequiredArgsConstructor
public class UserMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NonNull
    @Column(name = "message")
    private String message;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "read")
    private Boolean read;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_message_action", schema = "thing_tracker",
            joinColumns = @JoinColumn(name = "user_message_id"),
            inverseJoinColumns = @JoinColumn(name = "message_action_id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<MessageActionEntity> actions;

}
