package com.antilamer.thingTracker.repository;

import com.antilamer.thingTracker.dto.ExpenseSearchDTO;
import com.antilamer.thingTracker.model.ExpenseEntity;
import org.springframework.data.domain.Sort;
import java.util.Map;

public class ExpenseRepoImpl extends AbstractCustomRepoImpl<ExpenseEntity, ExpenseSearchDTO> implements AbstractCustomRepo<ExpenseEntity, ExpenseSearchDTO> {

    @Override
    protected void initSearchWhereParameters(ExpenseSearchDTO filter, Map<String, Object> params) {
        if (filter != null) {
            if (filter.getDateFrom() != null && filter.getDateTo() != null) {
                params.put("dateFrom", filter.getDateFrom());
                params.put("dateTo", filter.getDateTo());
            }

            if (filter.getSelectGroupmateIds() != null)
                params.put("selectGroupmateIds", filter.getSelectGroupmateIds());

            if (filter.getExpenseTypes() != null && filter.getExpenseTypes().size() > 0)
                params.put("expenseTypes", filter.getExpenseTypes());

        }
    }

    @Override
    protected String getSearchWhereStatement(ExpenseSearchDTO filter) {
        StringBuilder whereStatementBuilder = new StringBuilder();

        if (filter.getDateFrom() != null && filter.getDateTo() != null)
            whereStatementBuilder.append(" AND  U.date BETWEEN :dateFrom AND :dateTo ");

        if (filter.getSelectGroupmateIds() != null)
            whereStatementBuilder.append(" AND  user.id in :selectGroupmateIds ");

        if (filter.getExpenseTypes() != null && filter.getExpenseTypes().size() > 0)
            whereStatementBuilder.append(" AND  expenseTypeDict.name in :expenseTypes ");

        return whereStatementBuilder.toString();
    }


    @Override
    protected String getJoinForFetchStatement(ExpenseSearchDTO filter) {
        StringBuilder statementBuilder = new StringBuilder();

        statementBuilder.append(" JOIN U.user user ");
        if (filter.getExpenseTypes() != null && filter.getExpenseTypes().size() > 0) {
            statementBuilder.append(" JOIN  U.expenseTypeDict expenseTypeDict ");
        }

        return statementBuilder.toString();
    }

    @Override
    protected String getJoinForCountStatement(ExpenseSearchDTO filter) {
        StringBuilder statementBuilder = new StringBuilder();

        statementBuilder.append(" JOIN U.user user ");
        if (filter.getExpenseTypes() != null && filter.getExpenseTypes().size() > 0) {
            statementBuilder.append(" JOIN  U.expenseTypeDict expenseTypeDict ");
        }

        return statementBuilder.toString();
    }


    @Override
    protected String makeOrderByProperty(Sort.Order order, ExpenseSearchDTO filter) {
        return "U." + order.getProperty() + " " + order.getDirection().name();
    }
}
