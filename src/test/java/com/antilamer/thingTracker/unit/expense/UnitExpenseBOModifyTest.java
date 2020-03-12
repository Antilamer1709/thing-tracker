package com.antilamer.thingTracker.unit.expense;

import com.antilamer.thingTracker.Utils;
import com.antilamer.thingTracker.domain.ExpenseEntity;
import com.antilamer.thingTracker.exception.UnauthorizedException;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.repository.ExpenseRepo;
import com.antilamer.thingTracker.service.AuthenticationBO;
import com.antilamer.thingTracker.service.ExpenseBOImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class UnitExpenseBOModifyTest {

    @InjectMocks
    private ExpenseBOImpl expenseBO;

    @Mock
    private AuthenticationBO authenticationBO;

    @Mock
    private ExpenseRepo expenseRepo;


    // deleteExpense
    @Test
    public void deleteExpense_Valid() throws ValidationException, UnauthorizedException {
        ExpenseEntity expenseEntity = new ExpenseEntity();
        expenseEntity.setUser(Utils.createDefaultUser());
        given(expenseRepo.findById(1)).willReturn(Optional.of(expenseEntity));

        expenseBO.deleteExpense(1);
    }
}
