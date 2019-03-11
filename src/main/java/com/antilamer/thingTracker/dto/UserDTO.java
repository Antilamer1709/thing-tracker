package com.antilamer.thingTracker.dto;

import com.antilamer.thingTracker.model.UserEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO implements Serializable {

    private Integer id;

    private String email;

    private String fullName;

    private String password;

    private List<String> roles;


    public UserDTO() {
    }

    public UserDTO(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.email = userEntity.getEmail();
        this.fullName = userEntity.getFullName();
        this.roles = new ArrayList<>();
    }

}
