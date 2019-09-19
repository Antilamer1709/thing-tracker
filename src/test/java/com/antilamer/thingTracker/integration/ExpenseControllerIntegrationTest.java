package com.antilamer.thingTracker.integration;

import com.antilamer.thingTracker.config.TestConfig;
import com.antilamer.thingTracker.controller.ExpenseController;
import com.antilamer.thingTracker.dto.ExpenseDTO;
import com.antilamer.thingTracker.dto.ExpenseSearchChartDTO;
import com.antilamer.thingTracker.dto.ExpenseSearchDTO;
import com.antilamer.thingTracker.service.ExpenseBO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ExpenseController.class, ObjectMapper.class, TestConfig.class})
@EnableWebMvc
@AutoConfigureMockMvc
public class ExpenseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ExpenseBO expenseBO;


    // createExpense
    @Test
    @WithMockUser(username = "user1", password = "user1", roles = "USER")
    public void userSavesValidExpense_thenReturns200() throws Exception {
        ExpenseDTO expenseDTO = new ExpenseDTO();
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

        ArgumentCaptor<ExpenseDTO> userCaptor = ArgumentCaptor.forClass(ExpenseDTO.class);
        verify(expenseBO, times(1)).createExpense(userCaptor.capture());

        assertEquals("Prices are not equals", 500, userCaptor.getValue().getPrice());
        assertEquals("Types are not equals", "Food", userCaptor.getValue().getTypes().get(0));
    }


    // searchExpenseTypes
    @Test
    @WithMockUser(username = "user1", password = "user1", roles = "USER")
    public void userSearchExpenseWithPredicate_thenReturns200() throws Exception {
        mockMvc.perform(get("/api/expense/types")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .param("predicate", "Foo"))
                .andExpect(status().isOk());

        ArgumentCaptor<String> userCaptor = ArgumentCaptor.forClass(String.class);
        verify(expenseBO, times(1)).searchExpenseTypes(userCaptor.capture());

        assertEquals("Predicates are not equals", "Foo", userCaptor.getValue());
    }


    // searchChart
    @Test
    @WithMockUser(username = "user1", password = "user1", roles = "USER")
    public void userSearchChartEmpty_thenReturns200() throws Exception {
        ExpenseSearchChartDTO resultDTO = new ExpenseSearchChartDTO();
        given(expenseBO.searchChart(any())).willReturn(resultDTO);
        ExpenseSearchDTO expenseSearchDTO = new ExpenseSearchDTO();

        MvcResult mvcResult = mockMvc.perform(post("/api/expense/search/chart")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(expenseSearchDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(objectMapper.writeValueAsString(resultDTO)).isEqualToIgnoringWhitespace(actualResponseBody);

        ArgumentCaptor<ExpenseSearchDTO> userCaptor = ArgumentCaptor.forClass(ExpenseSearchDTO.class);
        verify(expenseBO, times(1)).searchChart(userCaptor.capture());
    }

}
