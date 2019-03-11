package com.antilamer.thingTracker.dto;

import lombok.Data;

@Data
public class RegistrationDTO {

    private String fullName;

    private String email;

    private String password;

    private String confirmPassword;

    public boolean hasNullFields() {
        return email == null || fullName == null || password == null || confirmPassword == null;
    }

    public boolean hasEmptyFields() {
        return email.isEmpty() || fullName.isEmpty() || password.isEmpty()
                || confirmPassword.isEmpty();
    }

}
