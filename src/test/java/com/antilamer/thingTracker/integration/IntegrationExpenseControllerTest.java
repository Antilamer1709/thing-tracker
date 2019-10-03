package com.antilamer.thingTracker.integration;

import com.antilamer.thingTracker.Utils;
import com.antilamer.thingTracker.controller.ExpenseController;
import com.antilamer.thingTracker.dto.ExpenseDTO;
import com.antilamer.thingTracker.dto.ExpenseSearchChartDTO;
import com.antilamer.thingTracker.dto.ExpenseSearchDTO;
import com.antilamer.thingTracker.dto.SearchDTO;
import com.antilamer.thingTracker.dto.response.ResponseDTO;
import com.antilamer.thingTracker.model.ExpenseEntity;
import com.antilamer.thingTracker.model.UserEntity;
import com.antilamer.thingTracker.repository.*;
import com.antilamer.thingTracker.security.JwtTokenProvider;
import com.antilamer.thingTracker.service.AuthenticationBOImpl;
import com.antilamer.thingTracker.service.ExpenseBO;
import com.antilamer.thingTracker.service.ExpenseBOImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static com.antilamer.thingTracker.Utils.doTestPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(value = {ExpenseController.class, ObjectMapper.class, ExpenseBOImpl.class, AuthenticationBOImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IntegrationExpenseControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ExpenseBO expenseBO;

    @Autowired
    private ExpenseRepo expenseRepo;

    @Autowired
    private ExpenseTypeDictRepo expenseTypeDictRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GroupRepo groupRepo;

    @Autowired
    private RoleRepo roleRepo;

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

        mockMvc = MockMvcBuilders.standaloneSetup(new ExpenseController(expenseBO)).build();
    }


    // createExpense
    @Test
    public void userSavesValidExpense_thenReturns200() throws Exception {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setPrice(500);
        expenseDTO.getTypes().add("Food");


        mockMvc.perform(post("/api/expense")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(expenseDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }


    // searchExpenseTypes
    @Test
    public void userSearchExpenseWithPredicate_thenReturns200() throws Exception {
        mockMvc.perform(get("/api/expense/types")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .param("predicate", "Foo"))
                .andExpect(status().isOk());
    }


    // searchChart
    @Test
    public void userSearchChartPopulated_thenReturns200() throws Exception {
        userSavesValidExpense_thenReturns200();
        ExpenseSearchChartDTO expectedResult = new ExpenseSearchChartDTO();
        expectedResult.getData().put("Food", 500);
        ExpenseSearchDTO expenseSearchDTO = new ExpenseSearchDTO();

        MvcResult mvcResult = doTestPost(mockMvc, objectMapper, "/api/expense/search/chart", expenseSearchDTO);

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        ExpenseSearchChartDTO actualResponseDTO = objectMapper.readValue(actualResponseBody, ExpenseSearchChartDTO.class);
        assertThat(actualResponseDTO).isEqualTo(expectedResult);
    }


    // searchProfileExpenses
    @Test
    public void userSearchProfileExpensesPopulated_thenReturns200() throws Exception {
        userSavesValidExpense_thenReturns200();

        ExpenseSearchDTO expenseSearchDTO = new ExpenseSearchDTO();
        expenseSearchDTO.getSelectGroupmateIds().add(1);
        SearchDTO<ExpenseSearchDTO> searchDTO = new SearchDTO<>(expenseSearchDTO, 0, 10);

        MvcResult mvcResult = doTestPost(mockMvc, objectMapper, "/api/expense/search/profile", searchDTO);

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        ResponseDTO<List<ExpenseDTO>> responseDTO = objectMapper.readValue(actualResponseBody, ResponseDTO.class);
        assertThat(responseDTO.getData().size()).isEqualTo(1);
    }



    // deleteExpense
    @Test
    public void userDeleteExpense_thenReturns200() throws Exception {
        ExpenseEntity expenseEntity = new ExpenseEntity();
        expenseEntity.setPrice(99);
        expenseEntity.setDate(LocalDateTime.now());
        expenseEntity.setUser(Utils.createDefaultUser());
        expenseEntity = expenseRepo.save(expenseEntity);

        mockMvc.perform(delete("/api/expense/" + expenseEntity.getId())
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

}
