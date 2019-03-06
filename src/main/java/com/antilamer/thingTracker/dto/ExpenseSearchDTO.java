package com.antilamer.thingTracker.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExpenseSearchDTO {

    LocalDateTime dateFrom;

    LocalDateTime dateTo;

}
