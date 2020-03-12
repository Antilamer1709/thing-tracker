package com.antilamer.thingTracker.dto;

import com.antilamer.thingTracker.domain.ExpenseEntity;
import com.antilamer.thingTracker.domain.ExpenseTypeDictEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ExpenseDTO {

    private Integer id;

    private Integer price;

    private String comment;

    private LocalDateTime date;

    private UserDTO creator;

    private List<String> types;


    public ExpenseDTO() {
        types = new ArrayList<>();
    }

    public ExpenseDTO(ExpenseEntity expenseEntity) {
        super();
        this.id = expenseEntity.getId();
        this.price = expenseEntity.getPrice();
        this.comment = expenseEntity.getComment();
        this.date = expenseEntity.getDate();
        this.types = expenseEntity.getExpenseTypeDict().stream().map(ExpenseTypeDictEntity::getName).collect(Collectors.toList());
    }

}
