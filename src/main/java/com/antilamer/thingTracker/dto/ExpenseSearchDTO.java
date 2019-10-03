package com.antilamer.thingTracker.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class ExpenseSearchDTO {

    private Integer id;

    private Integer price;

    private String comment;

    private LocalDateTime dateFrom;

    private LocalDateTime dateTo;

    private List<SelectGroupmateDTO> selectGroupmates;

    private Set<Integer> selectGroupmateIds;

    private List<String> expenseTypes;

    public ExpenseSearchDTO() {
        this.selectGroupmateIds = new HashSet<>();
    }

}
