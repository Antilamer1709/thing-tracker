package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.ExpenseDTO;
import com.antilamer.thingTracker.exception.ValidationException;

public interface ExpenseBO {

    void createExpense(ExpenseDTO expenseDTO) throws ValidationException;

}
