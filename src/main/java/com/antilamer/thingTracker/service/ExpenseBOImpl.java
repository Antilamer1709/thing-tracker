package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.ExpenseDTO;
import com.antilamer.thingTracker.dto.ExpenseSearchChartDTO;
import com.antilamer.thingTracker.dto.ExpenseSearchDTO;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.model.ExpenseEntity;
import com.antilamer.thingTracker.model.ExpenseTypeDictEntity;
import com.antilamer.thingTracker.model.UserEntity;
import com.antilamer.thingTracker.repository.ExpenseRepo;
import com.antilamer.thingTracker.repository.ExpenseTypeDictRepo;
import com.sun.xml.internal.ws.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExpenseBOImpl implements ExpenseBO {

    private final ExpenseRepo expenseRepo;
    private final ExpenseTypeDictRepo expenseTypeDictRepo;
    private final AuthenticationBO authenticationBO;

    @Autowired
    public ExpenseBOImpl(
            ExpenseRepo expenseRepo,
            ExpenseTypeDictRepo expenseTypeDictRepo,
            AuthenticationBO authenticationBO) {
        this.expenseRepo = expenseRepo;
        this.expenseTypeDictRepo = expenseTypeDictRepo;
        this.authenticationBO = authenticationBO;
    }


    @Override
    @Transactional
    public void createExpense(ExpenseDTO expenseDTO) throws ValidationException {
        validateExpense(expenseDTO);
        ExpenseEntity expenseEntity = new ExpenseEntity();
        initExpenseEntity(expenseEntity, expenseDTO);
        expenseRepo.save(expenseEntity);
    }

    private void validateExpense(ExpenseDTO expenseDTO) throws ValidationException {
        if (expenseDTO.getPrice() == null) {
            throw new ValidationException("Price is empty!");
        }
        if (expenseDTO.getTypes() == null || expenseDTO.getTypes().isEmpty()) {
            throw new ValidationException("Types are empty!");
        }
    }

    private void initExpenseEntity(ExpenseEntity expenseEntity, ExpenseDTO expenseDTO) {
        UserEntity userEntity = authenticationBO.getLoggedUser();

        expenseEntity.setUser(userEntity);
        expenseEntity.setPrice(expenseDTO.getPrice());
        expenseEntity.setComment(expenseDTO.getComment());
        expenseEntity.setDate(LocalDateTime.now());
        initExpenseTypes(expenseEntity, expenseDTO.getTypes());
    }

    private void initExpenseTypes(ExpenseEntity expenseEntity, List<String> types) {
        List<ExpenseTypeDictEntity> expenseTypes = new ArrayList<>();

        types.forEach(x -> {
            ExpenseTypeDictEntity typeDict = expenseTypeDictRepo
                    .findByNameIgnoreCase(x.trim())
                    .orElseGet(() -> createExpenseTypeDict(x));
            typeDict.setUsedCount(typeDict.getUsedCount() + 1);
            typeDict = expenseTypeDictRepo.save(typeDict);
            expenseTypes.add(typeDict);
        });

        expenseEntity.setExpenseTypeDict(expenseTypes);
    }

    private ExpenseTypeDictEntity createExpenseTypeDict(String name) {
        ExpenseTypeDictEntity typeDict = new ExpenseTypeDictEntity();
        UserEntity userEntity = authenticationBO.getLoggedUser();

        typeDict.setUser(userEntity);
        typeDict.setName(StringUtils.capitalize(name.trim()));
        typeDict.setUsedCount(0);

        return expenseTypeDictRepo.save(typeDict);
    }


    @Override
    public List<String> searchExpenseTypes(String predicate) {
        return expenseTypeDictRepo.findTop5ByNameContainingIgnoreCase(predicate.trim())
                .stream().map(ExpenseTypeDictEntity::getName)
                .collect(Collectors.toList());
    }


    @Override
    public ExpenseSearchChartDTO searchChart(ExpenseSearchDTO expenseSearchDTO) {
        //todo refactor
        ExpenseSearchChartDTO searchChartDTO = new ExpenseSearchChartDTO();
        UserEntity userEntity = authenticationBO.getLoggedUser();
        List<ExpenseEntity> expenseEntities = expenseRepo.searchChart(userEntity.getId(), expenseSearchDTO.getDateFrom(), expenseSearchDTO.getDateTo());

        expenseEntities.forEach(x -> {
            Integer price = searchChartDTO.getData().put(x.getExpenseTypeDict().get(0).getName(), x.getPrice());
            if (price != null) {
                Integer sum = searchChartDTO.getData().get(x.getExpenseTypeDict().get(0).getName());
                searchChartDTO.getData().put(x.getExpenseTypeDict().get(0).getName(), price + sum);
            }
        });

        return searchChartDTO;
    }
}
