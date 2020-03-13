package com.antilamer.thingTracker.unit.user;

import com.antilamer.thingTracker.controller.UserController;
import com.antilamer.thingTracker.service.UserBO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.antilamer.thingTracker.Utils.doTestGet;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UnitUserControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserBO userBO;

    @InjectMocks
    private UserController userController;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }


    @Test
    public void getUserById_thenReturns200() throws Exception {
        Integer userId = 1;
        doTestGet(mockMvc, objectMapper, "/api/user/1");

        ArgumentCaptor<Integer> userCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(userBO, times(1)).getUser(userCaptor.capture());

        assertEquals("Ids are not equals", userId, userCaptor.getValue());
    }

    @Test
    public void searchUserSuggestions_thenReturns200() throws Exception {
        String predicate = "user1";
        doTestGet(mockMvc, objectMapper, "/api/user/autocomplete?predicate=" + predicate);

        ArgumentCaptor<String> predicateCaptor = ArgumentCaptor.forClass(String.class);
        verify(userBO, times(1)).searchUserSuggestions(predicateCaptor.capture());

        assertEquals("Predicates are not equals", predicate, predicateCaptor.getValue());
    }


}
