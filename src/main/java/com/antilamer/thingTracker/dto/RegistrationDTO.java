package com.antilamer.thingTracker.dto;

import lombok.Data;

@Data
public class RegistrationDTO {

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private String confirmPassword;

    public boolean hasNullFields() {
        return username == null || firstName == null || lastName == null || password == null || confirmPassword == null;
    }

    public boolean hasEmptyFields() {
        return username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || password.isEmpty()
                || confirmPassword.isEmpty();
    }

}
