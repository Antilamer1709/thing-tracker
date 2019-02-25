package com.antilamer.thingTracker.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDTO implements Serializable {

    private Integer id;

    private String username;

    private String firstName;

    private String lastName;

    private String password;

    private List<String> roles;


    public UserDTO() {
    }

}
