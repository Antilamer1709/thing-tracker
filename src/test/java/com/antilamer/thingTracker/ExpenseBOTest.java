package com.antilamer.thingTracker;

import com.antilamer.thingTracker.dto.ExpenseDTO;
import com.antilamer.thingTracker.dto.ExpenseSearchChartDTO;
import com.antilamer.thingTracker.dto.ExpenseSearchDTO;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.model.ExpenseTypeDictEntity;
import com.antilamer.thingTracker.repository.*;
import com.antilamer.thingTracker.security.JwtTokenProvider;
import com.antilamer.thingTracker.service.AuthenticationBO;
import com.antilamer.thingTracker.service.AuthenticationBOImpl;
import com.antilamer.thingTracker.service.ExpenseBO;
import com.antilamer.thingTracker.service.ExpenseBOImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ExpenseBOImpl.class, AuthenticationBOImpl.class, ExpenseBOTest.TestConfig.class})
public class ExpenseBOTest {

    @Autowired
    private ExpenseBO expenseBO;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationBO authenticationBO;

    @MockBean
    private ExpenseRepo expenseRepo;

    @MockBean
    private ExpenseTypeDictRepo expenseTypeDictRepo;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private GroupRepo groupRepo;

    @MockBean
    private RoleRepo roleRepo;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider tokenProvider;


    @TestConfiguration
    static class TestConfig {

        @Bean
        public UserDetailsService userDetailsService() {
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
            UserDetails userDetails = new User("user", "user123", Collections.singletonList(authority));
            return new InMemoryUserDetailsManager(Collections.singletonList(userDetails));
        }

    }


    @Test
    public void whenCreateExpense_Valid() throws ValidationException {
        ExpenseTypeDictEntity type = new ExpenseTypeDictEntity();
        type.setName("Food");
        type.setUsedCount(0);

        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setPrice(500);
        expenseDTO.setTypes(new ArrayList<>());
        expenseDTO.getTypes().add(type.getName());

        given(expenseTypeDictRepo.findByNameIgnoreCase("Food")).willReturn(Optional.of(type));
        given(expenseTypeDictRepo.save(type)).willReturn(type);

        expenseBO.createExpense(expenseDTO);
    }

    @Test
    public void whenCreateExpense_ValidWithNoType() throws ValidationException {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setPrice(70000);
        expenseDTO.setTypes(new ArrayList<>());
        expenseDTO.getTypes().add("Car");

        given(expenseTypeDictRepo.findByNameIgnoreCase(any())).willReturn(Optional.empty());
        given(expenseTypeDictRepo.save(any())).willAnswer(i -> i.getArguments()[0]);

        expenseBO.createExpense(expenseDTO);
    }

    @Test(expected = ValidationException.class)
    public void whenCreateExpense_InvalidWithNoPrice() throws ValidationException {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setTypes(new ArrayList<>());
        expenseDTO.getTypes().add("Car");

        expenseBO.createExpense(expenseDTO);
    }

    @Test(expected = ValidationException.class)
    public void whenCreateExpense_InvalidWithNoType() throws ValidationException {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setPrice(70000);

        expenseBO.createExpense(expenseDTO);
    }


    @Test()
    public void whenSearchExpenseTypes_Valid() throws ValidationException {
        List<ExpenseTypeDictEntity> typeDictEntity = createTypeDictEntityList();
        given(expenseTypeDictRepo.findTop5ByNameContainingIgnoreCase("Tes")).willReturn(typeDictEntity);

        List<String> expenseTypes = expenseBO.searchExpenseTypes("Tes");

        assertThat(expenseTypes.size(), is(3));
        String testString = expenseTypes.stream().filter(x -> x.equals("Test")).findFirst()
                .orElseThrow(() -> new ValidationException("List does not contain 'Test' element"));
        assertThat(testString, is("Test"));
    }

    private List<ExpenseTypeDictEntity> createTypeDictEntityList() {
        List<ExpenseTypeDictEntity> expenseTypeDictEntity = new ArrayList<>();

        ExpenseTypeDictEntity entity = new ExpenseTypeDictEntity();
        entity.setName("Food");
        entity.setUsedCount(100);
        expenseTypeDictEntity.add(entity);

        entity = new ExpenseTypeDictEntity();
        entity.setName("Test");
        entity.setUsedCount(0);
        expenseTypeDictEntity.add(entity);

        entity = new ExpenseTypeDictEntity();
        entity.setName("Car");
        entity.setUsedCount(25);
        expenseTypeDictEntity.add(entity);

        return expenseTypeDictEntity;
    }


    @Test()
    @WithUserDetails()
    public void whenSearchChartWithNoData_ExpectEmpty() throws ValidationException {
        given(expenseRepo.getPagedData(any())).willReturn(new PageImpl<>(new ArrayList<>()));

        ExpenseSearchChartDTO searchChartDTO = expenseBO.searchChart(new ExpenseSearchDTO());

        assertThat(searchChartDTO.getData().size() == 0, is(true));
    }
}
