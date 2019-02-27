package com.antilamer.thingTracker.enums;

public enum UserRole {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String roleName;

    UserRole(final String newValue) {
        roleName = newValue;
    }

    public String getValue() { return roleName; }
}
