package com.antilamer.thingTracker.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExpenseSearchDTO {

    LocalDateTime dateFrom;

    LocalDateTime dateTo;

    List<SelectGroupmateDTO> selectGroupmates;

}
