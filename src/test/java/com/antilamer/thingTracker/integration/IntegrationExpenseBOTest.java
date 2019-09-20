package com.antilamer.thingTracker.integration;

import com.antilamer.thingTracker.Utils;
import com.antilamer.thingTracker.dto.ExpenseDTO;
import com.antilamer.thingTracker.dto.ExpenseSearchChartDTO;
import com.antilamer.thingTracker.dto.ExpenseSearchDTO;
import com.antilamer.thingTracker.dto.SelectGroupmateDTO;
import com.antilamer.thingTracker.enums.GroupmateType;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.model.ExpenseEntity;
import com.antilamer.thingTracker.model.ExpenseTypeDictEntity;
import com.antilamer.thingTracker.model.GroupEntity;
import com.antilamer.thingTracker.model.UserEntity;
import com.antilamer.thingTracker.repository.*;
import com.antilamer.thingTracker.security.JwtTokenProvider;
import com.antilamer.thingTracker.service.AuthenticationBOImpl;
import com.antilamer.thingTracker.service.ExpenseBO;
import com.antilamer.thingTracker.service.ExpenseBOImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(value = {ExpenseBOImpl.class, AuthenticationBOImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IntegrationExpenseBOTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ExpenseBO expenseBO;

    @Autowired
    private ExpenseRepo expenseRepo;

    @Autowired
    private ExpenseTypeDictRepo expenseTypeDictRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GroupRepo groupRepo;

    @Autowired
    private RoleRepo roleRepo;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider tokenProvider;

    private UserEntity loggedUser;

    @Before
    public void onSetUpTestUser() {
        loggedUser = Utils.createDefaultUser();

        Authentication auth = new UsernamePasswordAuthenticationToken(loggedUser, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }


    // createExpense
    @Test
    public void createExpense_Valid() throws ValidationException {
        ExpenseTypeDictEntity type = new ExpenseTypeDictEntity();
        type.setName("Food");
        type.setUsedCount(0);

        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setPrice(500);
        expenseDTO.getTypes().add(type.getName());

        expenseBO.createExpense(expenseDTO);
    }

    @Test
    public void createExpense_ValidWithNoType() throws ValidationException {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setPrice(70000);
        expenseDTO.getTypes().add("Car");

        expenseBO.createExpense(expenseDTO);
    }

    @Test(expected = ValidationException.class)
    public void createExpense_InvalidWithNoPrice() throws ValidationException {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.getTypes().add("Cow");

        expenseBO.createExpense(expenseDTO);
    }

    @Test(expected = ValidationException.class)
    public void createExpense_InvalidWithNoType() throws ValidationException {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setPrice(650);

        expenseBO.createExpense(expenseDTO);
    }


    // searchExpenseTypes
    @Test()
    public void searchExpenseTypes_Valid() throws ValidationException {
        createTypeDictEntityList();

        List<String> expenseTypes = expenseBO.searchExpenseTypes("Tes");

        assertThat(expenseTypes.size(), is(1));
        String testString = expenseTypes.stream().filter(x -> x.equals("Test")).findFirst()
                .orElseThrow(() -> new ValidationException("List does not contain 'Test' element"));
        assertThat(testString, is("Test"));
    }

    private List<ExpenseTypeDictEntity> createTypeDictEntityList() {
        List<ExpenseTypeDictEntity> expenseTypeList = new ArrayList<>();

        ExpenseTypeDictEntity entity = new ExpenseTypeDictEntity();
        entity.setId(1);
        entity.setName("Food");
        entity.setUsedCount(100);
        entity.setUser(loggedUser);
        expenseTypeList.add(entity);

        entity = new ExpenseTypeDictEntity();
        entity.setId(2);
        entity.setName("Test");
        entity.setUsedCount(0);
        entity.setUser(loggedUser);
        expenseTypeList.add(entity);

        entity = new ExpenseTypeDictEntity();
        entity.setId(3);
        entity.setName("Car");
        entity.setUsedCount(25);
        entity.setUser(loggedUser);
        expenseTypeList.add(entity);

        return expenseTypeDictRepo.saveAll(expenseTypeList);
    }


    //searchChart
    @Test()
    public void searchChartWithNoData_ExpectEmpty() throws ValidationException {
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
}
