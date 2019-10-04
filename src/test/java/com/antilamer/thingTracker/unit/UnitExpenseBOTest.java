package com.antilamer.thingTracker.unit;

import com.antilamer.thingTracker.Utils;
import com.antilamer.thingTracker.config.AppProperties;
import com.antilamer.thingTracker.dto.*;
import com.antilamer.thingTracker.enums.GroupmateType;
import com.antilamer.thingTracker.enums.UserRole;
import com.antilamer.thingTracker.exception.UnauthorizedException;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.model.ExpenseEntity;
import com.antilamer.thingTracker.model.ExpenseTypeDictEntity;
import com.antilamer.thingTracker.model.GroupEntity;
import com.antilamer.thingTracker.model.UserEntity;
import com.antilamer.thingTracker.repository.*;
import com.antilamer.thingTracker.security.JwtTokenProvider;
import com.antilamer.thingTracker.service.AuthenticationBO;
import com.antilamer.thingTracker.service.AuthenticationBOImpl;
import com.antilamer.thingTracker.service.ExpenseBO;
import com.antilamer.thingTracker.service.ExpenseBOImpl;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class UnitExpenseBOTest {

    private ExpenseBO expenseBO;

    @Mock
    private ExpenseRepo expenseRepo;

    @Mock
    private ExpenseTypeDictRepo expenseTypeDictRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private GroupRepo groupRepo;

    @Mock
    private RoleRepo roleRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private AppProperties appProperties;


    @Before
    public void onSetUpTestUser() {
        AuthenticationBO authenticationBO = new AuthenticationBOImpl(userRepo, roleRepo, passwordEncoder,
                authenticationManager, tokenProvider, appProperties);
        expenseBO = new ExpenseBOImpl(expenseRepo, expenseTypeDictRepo, userRepo, groupRepo, authenticationBO);

        UserEntity userDetails = Utils.createDefaultUser();

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }


    // createExpense
    @Test
    public void createExpense_Valid() throws ValidationException {
        ExpenseTypeDictEntity type = new ExpenseTypeDictEntity();
        type.setName("Stuff");
        type.setUsedCount(300);

        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setPrice(18000);
        expenseDTO.getTypes().add(type.getName());

        given(expenseTypeDictRepo.findByNameIgnoreCase("Stuff")).willReturn(Optional.of(type));
        given(expenseTypeDictRepo.save(type)).willReturn(type);

        expenseBO.createExpense(expenseDTO);
    }

    @Test
    public void createExpense_ValidWithNewType() throws ValidationException {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setPrice(70000);
        expenseDTO.getTypes().add("Car");

        given(expenseTypeDictRepo.findByNameIgnoreCase(any())).willReturn(Optional.empty());
        given(expenseTypeDictRepo.save(any())).willAnswer(i -> i.getArguments()[0]);

        expenseBO.createExpense(expenseDTO);
    }

    @Test
    public void createExpense_ValidWithNewTypeAndPastDate() throws ValidationException {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setPrice(5500);
        expenseDTO.setDate(LocalDateTime.of(2019, 1, 1, 0, 0));
        expenseDTO.getTypes().add("Computer");

        given(expenseTypeDictRepo.findByNameIgnoreCase("Computer")).willReturn(Optional.empty());
        given(expenseTypeDictRepo.save(any())).willAnswer(i -> i.getArguments()[0]);

        expenseBO.createExpense(expenseDTO);
    }

    @Test(expected = ValidationException.class)
    public void createExpense_InvalidWithNoPrice() throws ValidationException {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.getTypes().add("Car");

        expenseBO.createExpense(expenseDTO);
    }

    @Test(expected = ValidationException.class)
    public void createExpense_InvalidWithNoType() throws ValidationException {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setPrice(70000);

        expenseBO.createExpense(expenseDTO);
    }


    // searchExpenseTypes
    @Test()
    public void searchExpenseTypes_Valid() throws ValidationException {
        List<ExpenseTypeDictEntity> typeDictEntity = createTypeDictEntityList();
        given(expenseTypeDictRepo.findTop5ByNameContainingIgnoreCase("Tes")).willReturn(typeDictEntity);

        List<String> expenseTypes = expenseBO.searchExpenseTypes("Tes");

        assertThat(expenseTypes.size(), is(3));
        String testString = expenseTypes.stream().filter(x -> x.equals("Test")).findFirst()
                .orElseThrow(() -> new ValidationException("List does not contain 'Test' element"));
        assertThat(testString, is("Test"));
    }

    private List<ExpenseTypeDictEntity> createTypeDictEntityList() {
        List<ExpenseTypeDictEntity> expenseTypeDictEntity = new ArrayList<>();

        ExpenseTypeDictEntity entity = new ExpenseTypeDictEntity();
        entity.setId(1);
        entity.setName("Food");
        entity.setUsedCount(100);
        expenseTypeDictEntity.add(entity);

        entity = new ExpenseTypeDictEntity();
        entity.setId(2);
        entity.setName("Test");
        entity.setUsedCount(0);
        expenseTypeDictEntity.add(entity);

        entity = new ExpenseTypeDictEntity();
        entity.setId(3);
        entity.setName("Car");
        entity.setUsedCount(25);
        expenseTypeDictEntity.add(entity);

        return expenseTypeDictEntity;
    }


    //searchChart
    @Test()
    public void searchChartWithNoData_ExpectEmpty() throws ValidationException {
        given(expenseRepo.getPagedData(any())).willReturn(new PageImpl<>(new ArrayList<>()));

        ExpenseSearchChartDTO searchChartDTO = expenseBO.searchChart(new ExpenseSearchDTO());

        assertThat(searchChartDTO.getData().size() == 0, is(true));
    }

    @Test()
    public void searchChartWithGroup_ExpectEmpty() throws ValidationException {
        ExpenseSearchDTO searchDTO = processGivenSearchDTO(new PageImpl<>(new ArrayList<>()), GroupmateType.GROUP);

        ExpenseSearchChartDTO searchChartDTO = expenseBO.searchChart(searchDTO);

        assertThat(searchChartDTO.getData().size() == 0, is(true));
    }

    private ExpenseSearchDTO processGivenSearchDTO(PageImpl<ExpenseEntity> page, GroupmateType groupmateType) {
        UserEntity loggedUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        GroupEntity defaultGroup = loggedUser.getGroups().get(0);
        ExpenseSearchDTO searchDTO = createSearchChartDto(groupmateType);
        given(groupRepo.findById(1)).willReturn(Optional.of(defaultGroup));
        given(expenseRepo.getPagedData(any())).willReturn(page);
        return searchDTO;
    }

    private ExpenseSearchDTO createSearchChartDto(GroupmateType groupmateType) {
        ExpenseSearchDTO searchDTO = new ExpenseSearchDTO();
        SelectGroupmateDTO groupmateDTO = new SelectGroupmateDTO();

        groupmateDTO.setGroupId(1);
        groupmateDTO.setUserId(1);
        groupmateDTO.setLabel(groupmateType.equals(GroupmateType.GROUP) ? "Test users" : "user");
        groupmateDTO.setType(groupmateType);
        searchDTO.setSelectGroupmates(new ArrayList<>());
        searchDTO.getSelectGroupmates().add(groupmateDTO);

        return searchDTO;
    }

    @Test()
    public void searchChartWithGroup_ExpectOneElement() throws ValidationException {
        whenSearchChart_ExpectOneElement(GroupmateType.GROUP, new Integer[]{500});
    }

    private void whenSearchChart_ExpectOneElement(GroupmateType group, Integer[] prices) throws ValidationException {
        List<ExpenseEntity> expenseEntities = createGivenExpenseEntities(prices, true);
        ExpenseSearchChartDTO searchChartDTO = processSearchChart(group, expenseEntities);

        assertThat(searchChartDTO.getData().size() == 0, is(false));
        Integer priceSum = Arrays.stream(prices).reduce(0, Integer::sum);
        assertThat(searchChartDTO.getData().get("Food"), is(priceSum));
    }

    private ExpenseSearchChartDTO processSearchChart(GroupmateType group, List<ExpenseEntity> expenseEntities) throws ValidationException {
        PageImpl<ExpenseEntity> page = new PageImpl<>(expenseEntities);
        ExpenseSearchDTO searchDTO = processGivenSearchDTO(page, group);

        return expenseBO.searchChart(searchDTO);
    }

    private List<ExpenseEntity> createGivenExpenseEntities(Integer[] prices, boolean allTheSameType) {
        List<ExpenseEntity> expenseEntities = new ArrayList<>();

        for (int i = 0; i < prices.length; i++) {
            ExpenseEntity expenseEntity = new ExpenseEntity();
            expenseEntity.setId(i + 1);
            expenseEntity.setPrice(prices[i]);
            expenseEntity.setExpenseTypeDict(allTheSameType ? createTypeDictEntityList() : createDynamicTypeDictEntityList(i));
            expenseEntities.add(expenseEntity);
        }

        return expenseEntities;
    }

    private List<ExpenseTypeDictEntity> createDynamicTypeDictEntityList(int index) {
        List<ExpenseTypeDictEntity> populatedList = createTypeDictEntityList();

        return populatedList.subList(index, index + 1);
    }

    @Test()
    public void searchChartWithUser_ExpectOneElement() throws ValidationException {
        UserEntity loggedUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        given(userRepo.findById(1)).willReturn(Optional.of(loggedUser));

        whenSearchChart_ExpectOneElement(GroupmateType.USER, new Integer[]{500});
    }

    @Test()
    public void searchChartWithUserAndSeveralSameTypeExpenses_ExpectOneElement() throws ValidationException {
        UserEntity loggedUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        given(userRepo.findById(1)).willReturn(Optional.of(loggedUser));

        whenSearchChart_ExpectOneElement(GroupmateType.USER, new Integer[]{500, 1500, 300});
    }

    @Test(expected = ValidationException.class)
    public void searchChartWithUserFromAnotherGroup_ExpectValidationException() throws ValidationException {
        UserEntity user = Utils.createDefaultUser(2);
        given(userRepo.findById(1)).willReturn(Optional.of(user));

        whenSearchChart_ExpectOneElement(GroupmateType.USER, new Integer[]{777});
    }

    @Test()
    public void searchChartWithUserAndSeveralDifferentTypeExpenses_ExpectSeveralElements() throws ValidationException {
        UserEntity loggedUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        given(userRepo.findById(1)).willReturn(Optional.of(loggedUser));

        whenSearchChart_ExpectSeveralElements(GroupmateType.USER, new Integer[]{500, 1500, 300});
    }

    private void whenSearchChart_ExpectSeveralElements(GroupmateType group, Integer[] prices) throws ValidationException {
        List<ExpenseEntity> expenseEntities = createGivenExpenseEntities(prices, false);
        ExpenseSearchChartDTO searchChartDTO = processSearchChart(group, expenseEntities);

        assertThat(searchChartDTO.getData().size() == 0, is(false));
        assertThat(searchChartDTO.getData().get("Food"), is(prices[0]));
        assertThat(searchChartDTO.getData().get("Test"), is(prices[1]));
        assertThat(searchChartDTO.getData().get("Car"), is(prices[2]));
    }


    // searchProfileExpenses
    @Test
    public void searchProfileExpenses_WithNoData_ExpectEmpty() throws ValidationException, UnauthorizedException {
        given(expenseRepo.getPagedData(any())).willReturn(new PageImpl<>(new ArrayList<>()));
        given(userRepo.findById(any())).willReturn(Optional.of(Utils.createDefaultUser()));

        val result = expenseBO.searchProfileExpenses(createSearchProfileDTO(null));

        assertThat(result.getData().size() == 0, is(true));
    }

    @Test
    public void searchProfileExpenses_Populated_ExpectOneElement() throws ValidationException, UnauthorizedException {
        List<ExpenseEntity> expenseEntities = createGivenExpenseEntities(new Integer[]{280}, true);
        given(expenseRepo.getPagedData(any())).willReturn(new PageImpl<>(expenseEntities));
        given(userRepo.findById(1)).willReturn(Optional.of(Utils.createDefaultUser()));

        val result = expenseBO.searchProfileExpenses(createSearchProfileDTO(null));

        assertThat(result.getData().size() == 1, is(true));
        assertThat(result.getData().get(0).getPrice(), is(280));
        assertThat(result.getData().get(0).getTypes().get(0), is("Food"));
    }

    @Test
    public void searchProfileExpenses_Populated_ExpectTwoElements() throws ValidationException, UnauthorizedException {
        List<ExpenseEntity> expenseEntities = createGivenExpenseEntities(new Integer[]{600, 800}, false);
        given(expenseRepo.getPagedData(any())).willReturn(new PageImpl<>(expenseEntities));
        given(userRepo.findById(1)).willReturn(Optional.of(Utils.createDefaultUser()));

        val result = expenseBO.searchProfileExpenses(createSearchProfileDTO(null));

        assertThat(result.getData().size() == 2, is(true));
        assertThat(result.getData().get(0).getPrice(), is(600));
        assertThat(result.getData().get(0).getTypes().get(0), is("Food"));
        assertThat(result.getData().get(1).getPrice(), is(800));
        assertThat(result.getData().get(1).getTypes().get(0), is("Test"));
    }

    private SearchDTO<ExpenseSearchDTO> createSearchProfileDTO(Integer userId) {
        ExpenseSearchDTO filter = new ExpenseSearchDTO();
        if (userId != null) {
            filter.getSelectGroupmateIds().add(userId);
        }
        return new SearchDTO<>(filter, 0, 10);
    }

    @Test(expected = ValidationException.class)
    public void searchProfileExpenses_NoUser_ExpectValidationException() throws ValidationException, UnauthorizedException {
        given(userRepo.findById(any())).willReturn(Optional.empty());

        expenseBO.searchProfileExpenses(createSearchProfileDTO(null));
    }

    @Test
    public void searchProfileExpenses_WithAdmin_ExpectTwoElements() throws ValidationException, UnauthorizedException {
        UserEntity userAdmin = Utils.createDefaultUser(1, UserRole.USER, UserRole.ADMIN);
        Authentication auth = new UsernamePasswordAuthenticationToken(userAdmin, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        List<ExpenseEntity> expenseEntities = createGivenExpenseEntities(new Integer[]{999, 777}, true);
        given(expenseRepo.getPagedData(any())).willReturn(new PageImpl<>(expenseEntities));
        given(userRepo.findById(2)).willReturn(Optional.of(Utils.createDefaultUser(2, UserRole.USER)));

        val result = expenseBO.searchProfileExpenses(createSearchProfileDTO(2));

        assertThat(result.getData().size() == 2, is(true));
        assertThat(result.getData().get(0).getPrice(), is(999));
        assertThat(result.getData().get(0).getTypes().get(0), is("Food"));
        assertThat(result.getData().get(1).getPrice(), is(777));
        assertThat(result.getData().get(1).getTypes().get(0), is("Food"));
    }


    // deleteExpense
    @Test
    public void deleteExpense_Valid() throws ValidationException, UnauthorizedException {
        ExpenseEntity expenseEntity = new ExpenseEntity();
        expenseEntity.setUser(Utils.createDefaultUser());
        given(expenseRepo.findById(1)).willReturn(Optional.of(expenseEntity));

        expenseBO.deleteExpense(1);
    }
}
