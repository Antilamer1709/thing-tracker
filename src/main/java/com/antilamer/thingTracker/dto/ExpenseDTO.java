package com.antilamer.thingTracker.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExpenseDTO {

    private Integer id;

    private Integer price;

    private String comment;

    private UserDTO creator;

    private List<String> types;

}
