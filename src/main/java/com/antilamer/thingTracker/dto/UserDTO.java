package com.antilamer.thingTracker.dto;

import com.antilamer.thingTracker.model.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDTO implements Serializable {

    private Integer id;

    private String email;

    private String fullName;

    private String fullNameAndEmail;

    private String password;

    private List<String> roles;


    public UserDTO(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.email = userEntity.getEmail();
        this.fullName = userEntity.getFullName();
        this.fullNameAndEmail = userEntity.getFullName() + " (" + userEntity.getEmail() + ")";
        this.roles = new ArrayList<>();
    }

}
