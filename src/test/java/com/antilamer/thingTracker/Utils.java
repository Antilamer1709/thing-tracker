package com.antilamer.thingTracker;

import com.antilamer.thingTracker.model.GroupEntity;
import com.antilamer.thingTracker.model.RoleEntity;
import com.antilamer.thingTracker.model.UserEntity;

import java.util.ArrayList;

public class Utils {

    public static UserEntity createDefaultUser() {
        UserEntity userDetails = new UserEntity();
        userDetails.setId(1);
        userDetails.setEmail("user");
        userDetails.setFullName("Full metal user");
        userDetails.setPassword("user123");

        userDetails.setRoles(new ArrayList<>());
        RoleEntity role = new RoleEntity();
        role.setCode("ROLE_USER");
        userDetails.getRoles().add(role);

        userDetails.setGroups(new ArrayList<>());
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(1);
        groupEntity.setName("Test users");
        groupEntity.setCreator(userDetails);

        groupEntity.setUsers(new ArrayList<>());
        groupEntity.getUsers().add(userDetails);
        userDetails.getGroups().add(groupEntity);

        return userDetails;
    }

}
