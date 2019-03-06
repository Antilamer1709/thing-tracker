package com.antilamer.thingTracker.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ExpenseSearchChartDTO {

    private Map<String, Integer> data;


    public ExpenseSearchChartDTO() {
        this.data = new HashMap<>();
    }
}
