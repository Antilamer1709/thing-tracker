package com.antilamer.thingTracker.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
public class SearchDTO<T> {

    private int first;

    private int rows;

    private Integer sortOrder;

    private String sortField;

    private T filter;


    public SearchDTO(T filter, int first, int rows) {
        this.filter = filter;
        this.first = first;
        this.rows = rows;
    }

    public Pageable toPageable() {
        if (sortField != null) {
            return PageRequest.of(toPageIndex(), rows, toSortDirection(), sortField);
        } else {
            return PageRequest.of(toPageIndex(), rows, Sort.Direction.DESC, "id");
        }
    }

    public int toPageIndex() {
        return (first + rows) / rows - 1;
    }

    public Sort.Direction toSortDirection() {
        return sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC;
    }

}
