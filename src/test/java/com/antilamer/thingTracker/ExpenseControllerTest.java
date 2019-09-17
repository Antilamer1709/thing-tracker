package com.antilamer.thingTracker;

import com.antilamer.thingTracker.controller.ExpenseController;
import com.antilamer.thingTracker.dto.ExpenseDTO;
import com.antilamer.thingTracker.service.ExpenseBO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ExpenseController.class, ObjectMapper.class})
@EnableWebMvc
@AutoConfigureMockMvc
public class ExpenseControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ExpenseBO expenseBO;


    @Test
    @WithMockUser(username = "user1", password = "user1", roles = "USER")
    public void whenSaveValidExpense_thenReturns200() throws Exception {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setId(1);
        expenseDTO.setPrice(500);
        expenseDTO.setTypes(new ArrayList<>());
        expenseDTO.getTypes().add("Food");


        mockMvc.perform(post("/api/expense")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(expenseDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

}
