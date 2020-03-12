package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.domain.ExpenseEntity;
import com.antilamer.thingTracker.domain.ExpenseTypeDictEntity;
import com.antilamer.thingTracker.domain.UserEntity;
import com.antilamer.thingTracker.dto.*;
import com.antilamer.thingTracker.dto.response.ResponseDTO;
import com.antilamer.thingTracker.enums.GroupmateType;
import com.antilamer.thingTracker.exception.UnauthorizedException;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.repository.ExpenseRepo;
import com.antilamer.thingTracker.repository.ExpenseTypeDictRepo;
import com.antilamer.thingTracker.repository.GroupRepo;
import com.antilamer.thingTracker.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class ExpenseBOImpl implements ExpenseBO {

    private final ExpenseRepo expenseRepo;
    private final ExpenseTypeDictRepo expenseTypeDictRepo;
    private final UserRepo userRepo;
    private final GroupRepo groupRepo;
    private final AuthenticationBO authenticationBO;


    @Override
    @Transactional
    public void addNewExpense(@Valid ExpenseDTO expenseDTO) {
        ExpenseEntity expenseEntity = createExpenseEntity(expenseDTO);
        expenseRepo.save(expenseEntity);
    }

    private ExpenseEntity createExpenseEntity(ExpenseDTO expenseDTO) {
        return new ExpenseEntity.Builder()
                .fromDTO(expenseDTO)
                .withUser(authenticationBO.getLoggedUser())
                .withExpenseTypes(findExpenseTypes(expenseDTO.getTypes()))
                .build();
    }

    private List<ExpenseTypeDictEntity> findExpenseTypes(List<String> types) {
        List<ExpenseTypeDictEntity> expenseTypes = new ArrayList<>();

        types.forEach(x -> {
            ExpenseTypeDictEntity typeDict = expenseTypeDictRepo
                    .findByNameIgnoreCase(x.trim())
                    .orElseGet(() -> createExpenseTypeDict(x));
            typeDict.incrementUsedCounter();
            typeDict = expenseTypeDictRepo.save(typeDict);
            expenseTypes.add(typeDict);
        });

        return expenseTypes;
    }

    private ExpenseTypeDictEntity createExpenseTypeDict(String name) {
        ExpenseTypeDictEntity typeDict = new ExpenseTypeDictEntity.Builder()
                .withUser(authenticationBO.getLoggedUser())
                .withName(name)
                .build();

        return expenseTypeDictRepo.save(typeDict);
    }


    @Override
    public List<String> searchExpenseTypes(String predicate) {
        return expenseTypeDictRepo.findTop5ByNameContainingIgnoreCase(predicate.trim())
                .stream().map(ExpenseTypeDictEntity::getName)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public ExpenseSearchChartDTO searchChart(ExpenseSearchDTO expenseSearchDTO) throws ValidationException {
        ExpenseSearchChartDTO searchChartDTO = new ExpenseSearchChartDTO();
        processSearchChartDTO(expenseSearchDTO);
        processUserIds(expenseSearchDTO);
        SearchDTO<ExpenseSearchDTO> searchDTO = new SearchDTO<>(expenseSearchDTO, 0, Integer.MAX_VALUE);

        List<ExpenseEntity> expenseEntities = expenseRepo.getPagedData(searchDTO).getContent();

        expenseEntities.forEach(x -> {
            Integer previousValue = searchChartDTO.getData().put(x.getExpenseTypeDict().get(0).getName(), x.getPrice());
            if (previousValue != null) {
                Integer newValue = searchChartDTO.getData().get(x.getExpenseTypeDict().get(0).getName());
                searchChartDTO.getData().put(x.getExpenseTypeDict().get(0).getName(), previousValue + newValue);
            }
        });

        return searchChartDTO;
    }

    private void processSearchChartDTO(ExpenseSearchDTO expenseSearchDTO) {
        if (expenseSearchDTO.getDateTo() == null) {
            expenseSearchDTO.setDateTo(LocalDateTime.now());
        }
        if (expenseSearchDTO.getDateFrom() == null) {
            expenseSearchDTO.setDateFrom(LocalDateTime.of(0, 1, 1, 0, 0));
        }
    }

    private void processUserIds(ExpenseSearchDTO expenseSearchDTO) throws ValidationException {
        Set<Integer> userIds = new HashSet<>();

        if (expenseSearchDTO.getSelectGroupmates() != null && !expenseSearchDTO.getSelectGroupmates().isEmpty()) {
            for (SelectGroupmateDTO groupmateDTO : expenseSearchDTO.getSelectGroupmates()) {
                if (groupmateDTO.getType().equals(GroupmateType.USER)) {
                    userIds.add(processUserId(groupmateDTO));
                }
                if (groupmateDTO.getType().equals(GroupmateType.GROUP)) {
                    userIds.addAll(processGroupId(groupmateDTO));
                }
            }
        } else {
            userIds.add(authenticationBO.getLoggedUser().getId());
        }

        expenseSearchDTO.getSelectGroupmateIds().addAll(userIds);
    }

    private Integer processUserId(SelectGroupmateDTO user) throws ValidationException {
        UserEntity userEntity = userRepo.findById(user.getUserId())
                .orElseThrow(() -> new ValidationException("There no such user with id: " + user.getUserId()));
        validateUserRelationToGroup(user, userEntity);

        return user.getUserId();
    }

    private void validateUserRelationToGroup(SelectGroupmateDTO groupmateDTO, UserEntity userEntity) throws ValidationException {
        if (userEntity.getGroups().stream().noneMatch(x -> x.getId().equals(groupmateDTO.getGroupId()))) {
            throw new ValidationException("User with id: " + groupmateDTO.getUserId() + " do not belong to selected group!");
        }
    }

    private List<Integer> processGroupId(SelectGroupmateDTO group) throws ValidationException {
        UserEntity userEntity = authenticationBO.getLoggedUser();

        validateUserRelationToGroup(group, userEntity);

        return groupRepo.findById(group.getGroupId())
                .orElseThrow(() -> new ValidationException("There no such group with id: " + group.getGroupId()))
                .getUsers().stream().map(UserEntity::getId).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public ResponseDTO<List<ExpenseDTO>> searchProfileExpenses(SearchDTO<ExpenseSearchDTO> searchDTO) throws UnauthorizedException, ValidationException {
        processProfileExpensesUserIds(searchDTO.getFilter());
        validateProfileExpenseSearchDTO(searchDTO.getFilter());

        val pagedExpenses = expenseRepo.getPagedData(searchDTO);
        return new ResponseDTO<>(
                pagedExpenses.getContent().stream().map(ExpenseDTO::new).collect(Collectors.toList()),
                pagedExpenses.getTotalElements(),
                pagedExpenses.getTotalPages()
        );
    }

    private void processProfileExpensesUserIds(ExpenseSearchDTO filter) {
        if (filter.getSelectGroupmateIds().size() == 0) {
            filter.getSelectGroupmateIds().add(authenticationBO.getLoggedUser().getId());
        }
    }

    private void validateProfileExpenseSearchDTO(ExpenseSearchDTO expenseSearchDTO) throws UnauthorizedException, ValidationException {
        for (Integer groupmateUserId : expenseSearchDTO.getSelectGroupmateIds()) {
            UserEntity userEntity = userRepo.findById(groupmateUserId).orElseThrow(() ->
                    new ValidationException("There no such user with id: " + groupmateUserId));
            UserEntity loggedUser = authenticationBO.getLoggedUser();

            // requested user is belong to the same group as logged user
            boolean belongsToSameGroup =
                    loggedUser.getGroups()
                            .stream()
                            .anyMatch(loggedUserGroup ->
                                    userEntity.getGroups()
                                            .stream()
                                            .anyMatch(requestUserGroup ->
                                                    requestUserGroup.getId().equals(loggedUserGroup.getId())));

            // Admin has access to all users
            if (!belongsToSameGroup) {
                authenticationBO.checkAdminAccess(loggedUser);
            }
        }
    }


    @Override
    @Transactional
    public void deleteExpense(Integer id) throws ValidationException, UnauthorizedException {
        ExpenseEntity expenseEntity = expenseRepo.findById(id)
                .orElseThrow(() -> new ValidationException("There no such expense with id: " + id));
        authenticationBO.checkUserAccess(expenseEntity.getUser());

        expenseRepo.delete(expenseEntity);
    }

}
