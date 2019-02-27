package com.antilamer.thingTracker.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "role", schema = "thing_tracker")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;
}
