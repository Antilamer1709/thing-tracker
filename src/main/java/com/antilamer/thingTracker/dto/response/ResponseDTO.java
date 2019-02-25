package com.antilamer.thingTracker.dto.response;

import lombok.Data;

@Data
public class ResponseDTO<T> {

    Long totalElements;

    Integer totalPages;

    T data;

    public ResponseDTO() {
    }

    public ResponseDTO(T data, Long totalElements, Integer totalPages) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.data = data;
    }
}
