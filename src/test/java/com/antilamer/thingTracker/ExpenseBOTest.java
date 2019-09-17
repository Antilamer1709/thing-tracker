package com.antilamer.thingTracker;

import com.antilamer.thingTracker.dto.ExpenseDTO;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.model.ExpenseTypeDictEntity;
import com.antilamer.thingTracker.repository.ExpenseRepo;
import com.antilamer.thingTracker.repository.ExpenseTypeDictRepo;
import com.antilamer.thingTracker.repository.GroupRepo;
import com.antilamer.thingTracker.repository.UserRepo;
import com.antilamer.thingTracker.service.AuthenticationBO;
import com.antilamer.thingTracker.service.ExpenseBO;
import com.antilamer.thingTracker.service.ExpenseBOImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExpenseBOImpl.class)
public class ExpenseBOTest {

    @Autowired
    private ExpenseBO expenseBO;

    @MockBean
    private ExpenseRepo expenseRepo;

    @MockBean
    private ExpenseTypeDictRepo expenseTypeDictRepo;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private GroupRepo groupRepo;

    @MockBean
    private AuthenticationBO authenticationBO;


    @Test
    public void whenSavesValidExpense() throws ValidationException {
        ExpenseTypeDictEntity type = new ExpenseTypeDictEntity();
        type.setName("Food");
        type.setUsedCount(0);

        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setPrice(500);
        expenseDTO.setTypes(new ArrayList<>());
        expenseDTO.getTypes().add(type.getName());

        when(expenseTypeDictRepo.findByNameIgnoreCase("Food")).thenReturn(Optional.of(type));
        when(expenseTypeDictRepo.save(type)).thenReturn(type);

        expenseBO.createExpense(expenseDTO);
    }

    @Test
    public void whenSavesValidExpenseWithNoType() throws ValidationException {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setPrice(70000);
        expenseDTO.setTypes(new ArrayList<>());
        expenseDTO.getTypes().add("Car");

        when(expenseTypeDictRepo.findByNameIgnoreCase(any())).thenReturn(Optional.empty());
        when(expenseTypeDictRepo.save(any())).thenAnswer(i -> i.getArguments()[0]);

        expenseBO.createExpense(expenseDTO);
    }

    @Test(expected = ValidationException.class)
    public void whenSavesInvalidExpenseWithNoPrice() throws ValidationException {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setTypes(new ArrayList<>());
        expenseDTO.getTypes().add("Car");

        expenseBO.createExpense(expenseDTO);
    }

    @Test(expected = ValidationException.class)
    public void whenSavesInvalidExpenseWithNoType() throws ValidationException {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setPrice(70000);

        expenseBO.createExpense(expenseDTO);
    }
}
