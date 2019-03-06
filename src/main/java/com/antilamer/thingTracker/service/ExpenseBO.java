package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.ExpenseDTO;
import com.antilamer.thingTracker.dto.ExpenseSearchChartDTO;
import com.antilamer.thingTracker.dto.ExpenseSearchDTO;
import com.antilamer.thingTracker.exception.ValidationException;

import java.util.List;

public interface ExpenseBO {

    void createExpense(ExpenseDTO expenseDTO) throws ValidationException;

    List<String> searchExpenseTypes(String predicate);

    ExpenseSearchChartDTO searchChart(ExpenseSearchDTO expenseSearchDTO);

}
