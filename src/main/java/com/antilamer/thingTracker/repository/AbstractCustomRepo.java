package com.antilamer.thingTracker.repository;

import com.antilamer.thingTracker.dto.SearchDTO;
import org.springframework.data.domain.PageImpl;

public interface AbstractCustomRepo<T, F> {

    PageImpl<T> getPagedData(SearchDTO<F> filter);

}
