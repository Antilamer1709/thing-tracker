package com.antilamer.thingTracker.dto.response;

import lombok.Data;

@Data
public class ResponseError {

    private int code;
    private String message;
    private Exception exception;


    public ResponseError(int code, String message, Exception exception) {
        this.code = code;
        this.message = message;
        this.exception = exception;
    }

}
