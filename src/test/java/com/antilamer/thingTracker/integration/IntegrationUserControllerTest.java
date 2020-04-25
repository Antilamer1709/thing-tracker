package com.antilamer.thingTracker.integration;

import com.antilamer.thingTracker.Utils;
import com.antilamer.thingTracker.controller.UserController;
import com.antilamer.thingTracker.domain.UserEntity;
import com.antilamer.thingTracker.security.JwtTokenProvider;
import com.antilamer.thingTracker.service.AuthenticationServiceImpl;
import com.antilamer.thingTracker.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(value = {UserController.class, UserService.class, AuthenticationServiceImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IntegrationUserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider tokenProvider;

    @Before
    public void onSetUpTestUser() {
        UserEntity loggedUser = Utils.createDefaultUser();

        Authentication auth = new UsernamePasswordAuthenticationToken(loggedUser, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
    }


    @Test
    public void userSavesValidExpense_thenReturns200() throws Exception {
        mockMvc.perform(get("/api/user/1")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    public void searchUserSuggestions_thenReturns200() throws Exception {
        mockMvc.perform(get("/api/user/autocomplete")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .param("predicate", "user")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

}
