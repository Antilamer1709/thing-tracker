package com.antilamer.thingTracker.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ResponseToMessageDTO {

    @NotNull
    private Boolean response;

    @NotNull
    private Integer messageId;

}
