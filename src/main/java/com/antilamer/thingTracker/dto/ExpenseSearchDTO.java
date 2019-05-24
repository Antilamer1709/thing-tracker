package com.antilamer.thingTracker.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class ExpenseSearchDTO {

    LocalDateTime dateFrom;

    LocalDateTime dateTo;

    List<SelectGroupmateDTO> selectGroupmates;

    Set<Integer> selectGroupmateIds;

    List<String> expenseTypes;

    public ExpenseSearchDTO() {
        this.selectGroupmateIds = new HashSet<>();
    }

}
