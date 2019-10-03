package com.antilamer.thingTracker.controller;

import com.antilamer.thingTracker.dto.ExpenseDTO;
import com.antilamer.thingTracker.dto.ExpenseSearchChartDTO;
import com.antilamer.thingTracker.dto.ExpenseSearchDTO;
import com.antilamer.thingTracker.dto.SearchDTO;
import com.antilamer.thingTracker.dto.response.ResponseDTO;
import com.antilamer.thingTracker.exception.UnauthorizedException;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.service.ExpenseBO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/expense")
public class ExpenseController {

    private final ExpenseBO expenseBO;


    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping(value = "")
    @ResponseStatus(value = HttpStatus.OK)
    @Async("threadPoolTaskExecutor")
    public void createExpense(@RequestBody ExpenseDTO expenseDTO) throws ValidationException {
        log.debug("*** createExpense() expenseDTO: " + expenseDTO);
        expenseBO.createExpense(expenseDTO);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @DeleteMapping(value = "{id}")
    public void deleteExpense(@PathVariable Integer id) throws UnauthorizedException, ValidationException {
        log.debug("*** deleteExpense() id: " + id);
        expenseBO.deleteExpense(id);
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
    public ExpenseSearchChartDTO searchChart(@RequestBody ExpenseSearchDTO expenseSearchDTO) throws ValidationException {
        log.debug("*** searchChart() expenseSearchDTO: " + expenseSearchDTO);
        return expenseBO.searchChart(expenseSearchDTO);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping(value = "/search/profile")
    public ResponseDTO<List<ExpenseDTO>> searchProfile(@RequestBody SearchDTO<ExpenseSearchDTO> searchDTO) throws ValidationException, UnauthorizedException {
        log.debug("*** searchProfile() searchDTO: " + searchDTO);
        return expenseBO.searchProfileExpenses(searchDTO);
    }

}
