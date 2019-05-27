package com.antilamer.thingTracker.model;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "group", schema = "thing_tracker")
public class GroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user; // group admin

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "group_user", schema = "thing_tracker",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<UserEntity> users;

}
