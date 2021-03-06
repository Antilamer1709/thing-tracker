package com.antilamer.thingTracker.unit.expense;

import com.antilamer.thingTracker.controller.ExpenseController;
import com.antilamer.thingTracker.dto.ExpenseDTO;
import com.antilamer.thingTracker.dto.ExpenseSearchChartDTO;
import com.antilamer.thingTracker.dto.ExpenseSearchDTO;
import com.antilamer.thingTracker.dto.SearchDTO;
import com.antilamer.thingTracker.dto.response.ResponseDTO;
import com.antilamer.thingTracker.service.ExpenseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.antilamer.thingTracker.Utils.doTestPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class UnitExpenseControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ExpenseService expenseService;

    @InjectMocks
    private ExpenseController expenseController;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(expenseController).build();
    }


    // createExpense
    @Test
    public void userSavesValidExpense_thenReturns200() throws Exception {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setPrice(1000);
        expenseDTO.getTypes().add("Cars");


        doTestPost(mockMvc, objectMapper, "/api/expense", expenseDTO);

        ArgumentCaptor<ExpenseDTO> userCaptor = ArgumentCaptor.forClass(ExpenseDTO.class);
        verify(expenseService, times(1)).addNewExpense(userCaptor.capture());

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
        verify(expenseService, times(1)).searchExpenseTypes(userCaptor.capture());

        assertEquals("Predicates are not equals", "Alc", userCaptor.getValue());
    }


    // searchChart
    @Test
    public void userSearchChartEmpty_thenReturns200() throws Exception {
        ExpenseSearchChartDTO resultDTO = new ExpenseSearchChartDTO();
        given(expenseService.searchChart(any())).willReturn(resultDTO);
        ExpenseSearchDTO expenseSearchDTO = new ExpenseSearchDTO();

        MvcResult mvcResult = doTestPost(mockMvc, objectMapper, "/api/expense/search/chart", expenseSearchDTO);

        thenValidateSearchExpense(mvcResult, objectMapper.writeValueAsString(resultDTO));

        ArgumentCaptor<ExpenseSearchDTO> userCaptor = ArgumentCaptor.forClass(ExpenseSearchDTO.class);
        verify(expenseService, times(1)).searchChart(userCaptor.capture());
    }


    // searchProfileExpenses
    @Test
    public void searchProfileExpensesEmpty_thenReturns200() throws Exception {
        List<ExpenseDTO> expectedData = new ArrayList<>();
        ResponseDTO<List<ExpenseDTO>> expectedResult = new ResponseDTO<>(expectedData, 0L, 0);
        given(expenseService.searchProfileExpenses(any())).willReturn(expectedResult);
        SearchDTO<ExpenseSearchDTO> searchDTO = new SearchDTO<>(new ExpenseSearchDTO(), 0, 10);

        MvcResult mvcResult = doTestPost(mockMvc, objectMapper, "/api/expense/search/profile", searchDTO);

        thenValidateSearchExpense(mvcResult, objectMapper.writeValueAsString(expectedResult));

        ArgumentCaptor<SearchDTO<ExpenseSearchDTO>> userCaptor = ArgumentCaptor.forClass(SearchDTO.class);
        verify(expenseService, times(1)).searchProfileExpenses(userCaptor.capture());
    }

    private void thenValidateSearchExpense(MvcResult mvcResult, String s) throws UnsupportedEncodingException {
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(s).isEqualToIgnoringWhitespace(actualResponseBody);
    }


    // deleteExpense
    @Test
    public void userDeletesValidExpense_thenReturns200() throws Exception {
        mockMvc.perform(delete("/api/expense/1")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        ArgumentCaptor<Integer> userCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(expenseService, times(1)).deleteExpense(userCaptor.capture());
    }

}
