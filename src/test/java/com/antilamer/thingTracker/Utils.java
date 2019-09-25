package com.antilamer.thingTracker;

import com.antilamer.thingTracker.model.GroupEntity;
import com.antilamer.thingTracker.model.RoleEntity;
import com.antilamer.thingTracker.model.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class Utils {

    public static UserEntity createDefaultUser() {
        return createDefaultUser(1);
    }

    public static UserEntity createDefaultUser(int userGroupId) {
        UserEntity userDetails = new UserEntity();
        userDetails.setId(userGroupId);
        userDetails.setEmail("integrationTestUser1");
        userDetails.setFullName("Full metal user");
        userDetails.setPassword("user123");

        userDetails.setRoles(new ArrayList<>());
        RoleEntity role = new RoleEntity();
        role.setCode("ROLE_USER");
        userDetails.getRoles().add(role);

        userDetails.setGroups(new ArrayList<>());
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(userGroupId);
        groupEntity.setName("Test users");
        groupEntity.setCreator(userDetails);

        groupEntity.setUsers(new ArrayList<>());
        groupEntity.getUsers().add(userDetails);
        userDetails.getGroups().add(groupEntity);

        return userDetails;
    }


    public static MvcResult doTestPost(MockMvc mockMvc, ObjectMapper objectMapper, String url, Object body) throws Exception {
        return mockMvc.perform(post(url)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(body)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();
    }

}
