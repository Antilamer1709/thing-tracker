package com.antilamer.thingTracker.unit;

import com.antilamer.thingTracker.controller.ExpenseController;
import com.antilamer.thingTracker.dto.ExpenseDTO;
import com.antilamer.thingTracker.dto.ExpenseSearchChartDTO;
import com.antilamer.thingTracker.dto.ExpenseSearchDTO;
import com.antilamer.thingTracker.service.ExpenseBO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.antilamer.thingTracker.Utils.doTestPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class UnitExpenseControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ExpenseBO expenseBO;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ExpenseController(expenseBO)).build();
    }


    // createExpense
    @Test
    public void userSavesValidExpense_thenReturns200() throws Exception {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setPrice(1000);
        expenseDTO.getTypes().add("Cars");


        doTestPost(mockMvc, objectMapper, "/api/expense", expenseDTO);

        ArgumentCaptor<ExpenseDTO> userCaptor = ArgumentCaptor.forClass(ExpenseDTO.class);
        verify(expenseBO, times(1)).createExpense(userCaptor.capture());

        assertEquals("Prices are not equals", 1000, userCaptor.getValue().getPrice());
        assertEquals("Types are not equals", "Cars", userCaptor.getValue().getTypes().get(0));
    }


    // searchExpenseTypes
    @Test
    public void userSearchExpenseWithPredicate_thenReturns200() throws Exception {
        mockMvc.perform(get("/api/expense/types")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .param("predicate", "Alc"))
                .andExpect(status().isOk());

        ArgumentCaptor<String> userCaptor = ArgumentCaptor.forClass(String.class);
        verify(expenseBO, times(1)).searchExpenseTypes(userCaptor.capture());

        assertEquals("Predicates are not equals", "Alc", userCaptor.getValue());
    }


    // searchChart
    @Test
    public void userSearchChartEmpty_thenReturns200() throws Exception {
        ExpenseSearchChartDTO resultDTO = new ExpenseSearchChartDTO();
        given(expenseBO.searchChart(any())).willReturn(resultDTO);
        ExpenseSearchDTO expenseSearchDTO = new ExpenseSearchDTO();

        MvcResult mvcResult = doTestPost(mockMvc, objectMapper, "/api/expense/search/chart", expenseSearchDTO);

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(objectMapper.writeValueAsString(resultDTO)).isEqualToIgnoringWhitespace(actualResponseBody);

        ArgumentCaptor<ExpenseSearchDTO> userCaptor = ArgumentCaptor.forClass(ExpenseSearchDTO.class);
        verify(expenseBO, times(1)).searchChart(userCaptor.capture());
    }

}
