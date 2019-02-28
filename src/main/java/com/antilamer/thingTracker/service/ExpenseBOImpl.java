package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.ExpenseDTO;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.model.ExpenseEntity;
import com.antilamer.thingTracker.model.ExpenseTypeDictEntity;
import com.antilamer.thingTracker.model.UserEntity;
import com.antilamer.thingTracker.repository.ExpenseRepo;
import com.antilamer.thingTracker.repository.ExpenseTypeDictRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        initExpenseTypes(expenseEntity, expenseDTO.getTypes());
    }

    private void initExpenseTypes(ExpenseEntity expenseEntity, List<String> types) {
        List<ExpenseTypeDictEntity> expenseTypes = new ArrayList<>();

        types.forEach(x -> {
            ExpenseTypeDictEntity typeDict = expenseTypeDictRepo
                    .findByNameIgnoreCase(x.trim())
                    .orElseGet(() -> createExpenseTypeDict(x));
            expenseTypes.add(typeDict);
        });

        expenseEntity.setExpenseTypeDict(expenseTypes);
    }

    private ExpenseTypeDictEntity createExpenseTypeDict(String name) {
        ExpenseTypeDictEntity typeDict = new ExpenseTypeDictEntity();
        UserEntity userEntity = authenticationBO.getLoggedUser();

        typeDict.setUser(userEntity);
        typeDict.setName(name);

        return expenseTypeDictRepo.save(typeDict);
    }


    @Override
    public List<String> searchExpenseTypes(String predicate) {
        return expenseTypeDictRepo.findTop5ByNameContainingIgnoreCase(predicate.trim())
                .stream().map(ExpenseTypeDictEntity::getName)
                .collect(Collectors.toList());
    }
}
