package com.antilamer.thingTracker.unit.expense;

import com.antilamer.thingTracker.Utils;
import com.antilamer.thingTracker.domain.ExpenseTypeDictEntity;
import com.antilamer.thingTracker.domain.UserEntity;
import com.antilamer.thingTracker.dto.ExpenseDTO;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.repository.ExpenseRepo;
import com.antilamer.thingTracker.repository.ExpenseTypeDictRepo;
import com.antilamer.thingTracker.service.AuthenticationBO;
import com.antilamer.thingTracker.service.ExpenseBOImpl;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class UnitExpenseBOCreateTest {

    private static final UserEntity loggedUser = Utils.createDefaultUser();
    private static ValidatorFactory validatorFactory;
    private static Validator validator;


    @InjectMocks
    private ExpenseBOImpl expenseBO;

    @Mock
    private AuthenticationBO authenticationBO;

    @Mock
    private ExpenseRepo expenseRepo;

    @Mock
    private ExpenseTypeDictRepo expenseTypeDictRepo;


    @BeforeClass
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public static void close() {
        validatorFactory.close();
    }


    @Before
    public void onSetUpGivenUser() {
        given(authenticationBO.getLoggedUser()).willReturn(loggedUser);
    }


    // createExpense
    @Test
    public void createExpense_Valid() throws ValidationException {
        ExpenseTypeDictEntity type = new ExpenseTypeDictEntity();
        type.setName("Stuff");
        type.setUsedCount(300);

        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setPrice(18000);
        expenseDTO.getTypes().add(type.getName());

        given(expenseTypeDictRepo.findByNameIgnoreCase("Stuff")).willReturn(Optional.of(type));
        given(expenseTypeDictRepo.save(type)).willReturn(type);

        expenseBO.addNewExpense(expenseDTO);
    }

    @Test
    public void createExpense_ValidWithNewType() throws ValidationException {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setPrice(70000);
        expenseDTO.getTypes().add("Car");

        given(expenseTypeDictRepo.findByNameIgnoreCase(any())).willReturn(Optional.empty());
        given(expenseTypeDictRepo.save(any())).willAnswer(i -> i.getArguments()[0]);

        expenseBO.addNewExpense(expenseDTO);
    }

    @Test
    public void createExpense_ValidWithNewTypeAndPastDate() throws ValidationException {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setPrice(5500);
        expenseDTO.setDate(LocalDateTime.of(2019, 1, 1, 0, 0));
        expenseDTO.getTypes().add("Computer");

        given(expenseTypeDictRepo.findByNameIgnoreCase("Computer")).willReturn(Optional.empty());
        given(expenseTypeDictRepo.save(any())).willAnswer(i -> i.getArguments()[0]);

        expenseBO.addNewExpense(expenseDTO);
    }

    @Test
    public void createExpense_InvalidWithNoPrice() {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.getTypes().add("Car");

        Set<ConstraintViolation<ExpenseDTO>> violations = validator.validate(expenseDTO);

        assertEquals(violations.size(), 1);
    }

    @Test
    public void createExpense_InvalidWithNoType() {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setPrice(70000);

        Set<ConstraintViolation<ExpenseDTO>> violations = validator.validate(expenseDTO);

        assertEquals(violations.size(), 1);
    }

}
