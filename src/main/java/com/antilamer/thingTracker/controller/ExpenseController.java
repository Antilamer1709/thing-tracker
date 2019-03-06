package com.antilamer.thingTracker.controller;

import com.antilamer.thingTracker.dto.ExpenseDTO;
import com.antilamer.thingTracker.dto.ExpenseSearchChartDTO;
import com.antilamer.thingTracker.dto.ExpenseSearchDTO;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.service.ExpenseBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("api/expense")
public class ExpenseController {

    private final ExpenseBO expenseBO;

    @Autowired
    public ExpenseController(
            ExpenseBO expenseBO) {
        this.expenseBO = expenseBO;
    }


    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping(value = "")
    @ResponseStatus(value = HttpStatus.OK)
    public void createExpense(@RequestBody ExpenseDTO expenseDTO) throws ValidationException {
        log.debug("*** createExpense() expenseDTO: " + expenseDTO);
        expenseBO.createExpense(expenseDTO);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping(value = "/types")
    public List<String> searchExpenseTypes(@RequestParam String predicate) {
        log.debug("*** searchExpenseTypes() predicate: " + predicate);
        return expenseBO.searchExpenseTypes(predicate);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping(value = "/search/chart")
    @ResponseStatus(value = HttpStatus.OK)
    public ExpenseSearchChartDTO searchChart(@RequestBody ExpenseSearchDTO expenseSearchDTO) {
        log.debug("*** searchChart() expenseSearchDTO: " + expenseSearchDTO);
        return expenseBO.searchChart(expenseSearchDTO);
    }

}
