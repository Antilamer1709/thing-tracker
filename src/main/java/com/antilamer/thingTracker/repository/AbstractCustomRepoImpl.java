package com.antilamer.thingTracker.repository;

import com.antilamer.thingTracker.dto.SearchDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Slf4j
public abstract class AbstractCustomRepoImpl<T, F> implements AbstractCustomRepo<T, F> {

    @PersistenceContext
    protected EntityManager entityManager;

    protected Class<T> clazz;

    protected String idName;


    public AbstractCustomRepoImpl() {
        this.clazz = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0]);
        try {
            Class<?> c = Class.forName(clazz.getName());
            for (Field field : c.getDeclaredFields()) {
                if (field.isAnnotationPresent(Column.class) && field.isAnnotationPresent(Id.class)) {
                    this.idName = field.getName();
                    break;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public PageImpl<T> getPagedData(SearchDTO<F> filter) {
        log.debug("*** AbstractPagedRepoImpl::getPagedData() start");
        Pageable pageable = filter.toPageable();
        Query countQuery = makePagedQuery(filter.getFilter(), pageable, true);
        log.debug("*** AbstractPagedRepoImpl::getPagedData() querying count: start");
        List<Long> countResult = countQuery.getResultList();
        Long total;
        if (countResult == null || countResult.isEmpty()) {
            log.debug("*** AbstractPagedRepoImpl::getPagedData() querying count: failed - null or empty, returning null");
            return new PageImpl<T>(new ArrayList<>());
        } else {
            total = countResult.get(0);
            log.debug("*** AbstractPagedRepoImpl::getPagedData() querying count: finished, found " + total + " elements");
        }

        int firstElem = pageable.getPageNumber() * pageable.getPageSize();
        log.debug("*** AbstractPagedRepoImpl::getPagedData() fetching [page: " + pageable.getPageNumber() + "][limit: " + pageable.getPageSize() + "][total: "
                + total + "][start index: " + firstElem + "]");

        log.debug("*** AbstractPagedRepoImpl::getPagedData() firing main query");
        Query query = makePagedQuery(filter.getFilter(), pageable, false);
        query.setFirstResult(firstElem);
        query.setMaxResults(pageable.getPageSize());
        List<T> result = query.getResultList();
        if (result == null) {
            log.debug("*** AbstractPagedRepoImpl::getPagedData() querying failed - null, returning empty page");
            return new PageImpl<T>(new ArrayList<>());
        } else {
            log.debug("*** AbstractPagedRepoImpl::getPagedData() end with success, returning list");
            return new PageImpl<T>(result, pageable, total);
        }
    }

    protected abstract String getSearchWhereStatement(F filter);

    protected abstract void initSearchWhereParameters(F filter, Map<String, Object> params);


    protected abstract String getJoinForFetchStatement(F filter);

    protected abstract String getJoinForCountStatement(F filter);

    protected Query makePagedQuery(F filter, Pageable pageable, boolean count) {
        StringBuilder queryBuilder = new StringBuilder();
        if (count) {
            queryBuilder.append("SELECT COUNT(DISTINCT U) FROM ")
                    .append(this.clazz.getName()).append(" U ")
                    .append(getJoinForCountStatement(filter))
                    .append(" WHERE 1=1 ");

        } else {
            queryBuilder.append("SELECT DISTINCT U FROM ").append(this.clazz.getName()).append(" U ")
                    .append(getJoinForFetchStatement(filter))
                    .append(" WHERE 1=1 ");

        }

        String whereStatement = getSearchWhereStatement(filter);
        queryBuilder.append(whereStatement);
        queryBuilder.append(getGroupByHavingPhrase(filter, count));

        if (!count) {
            queryBuilder.append(getOrderBy(pageable.getSort(), filter));
        }

        Map<String, Object> params = new TreeMap<>();
        initSearchWhereParameters(filter, params);
        Query query = entityManager.createQuery(queryBuilder.toString());
        params.forEach(query::setParameter);
        return query;

    }

    protected String getOrderBy(Sort sort, F filter) {
        StringBuilder orderByBuilder = new StringBuilder();

        String orderBy = StreamSupport.stream(sort.spliterator(), false)
                .map(i -> makeOrderByProperty(i, filter)).
                        collect(Collectors.joining(" , "));
        orderByBuilder.append(" ORDER BY ");
        orderByBuilder.append(orderBy);
        return orderByBuilder.toString();
    }

    protected String getGroupByHavingPhrase(F filter, boolean count) {
        return "";
    }

    protected abstract String makeOrderByProperty(Sort.Order order, F filter);


}
