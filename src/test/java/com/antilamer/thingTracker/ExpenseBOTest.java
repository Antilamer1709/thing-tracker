package com.antilamer.thingTracker;

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
import com.antilamer.thingTracker.service.AuthenticationBO;
import com.antilamer.thingTracker.service.AuthenticationBOImpl;
import com.antilamer.thingTracker.service.ExpenseBO;
import com.antilamer.thingTracker.service.ExpenseBOImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
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
@SpringBootTest(classes = {ExpenseBOImpl.class, AuthenticationBOImpl.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ExpenseBOTest {

    @Autowired
    private ExpenseBO expenseBO;

    @Autowired
    private AuthenticationBO authenticationBO;

    @MockBean
    private ExpenseRepo expenseRepo;

    @MockBean
    private ExpenseTypeDictRepo expenseTypeDictRepo;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private GroupRepo groupRepo;

    @MockBean
    private RoleRepo roleRepo;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider tokenProvider;


    @Before
    public void onSetUpTestUser() {
        UserEntity userDetails = Utils.createDefaultUser();

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null);
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
        expenseDTO.setTypes(new ArrayList<>());
        expenseDTO.getTypes().add(type.getName());

        given(expenseTypeDictRepo.findByNameIgnoreCase("Food")).willReturn(Optional.of(type));
        given(expenseTypeDictRepo.save(type)).willReturn(type);

        expenseBO.createExpense(expenseDTO);
    }

    @Test
    public void createExpense_ValidWithNoType() throws ValidationException {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setPrice(70000);
        expenseDTO.setTypes(new ArrayList<>());
        expenseDTO.getTypes().add("Car");

        given(expenseTypeDictRepo.findByNameIgnoreCase(any())).willReturn(Optional.empty());
        given(expenseTypeDictRepo.save(any())).willAnswer(i -> i.getArguments()[0]);

        expenseBO.createExpense(expenseDTO);
    }

    @Test(expected = ValidationException.class)
    public void createExpense_InvalidWithNoPrice() throws ValidationException {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setTypes(new ArrayList<>());
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
        PageImpl<ExpenseEntity> page = new PageImpl<>(expenseEntities);
        ExpenseSearchDTO searchDTO = processGivenSearchDTO(page, group);

        ExpenseSearchChartDTO searchChartDTO = expenseBO.searchChart(searchDTO);

        assertThat(searchChartDTO.getData().size() == 0, is(false));
        Integer priceSum = Arrays.stream(prices).reduce(0, Integer::sum);
        assertThat(searchChartDTO.getData().get("Food"), is(priceSum));
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
        PageImpl<ExpenseEntity> page = new PageImpl<>(expenseEntities);
        ExpenseSearchDTO searchDTO = processGivenSearchDTO(page, group);

        ExpenseSearchChartDTO searchChartDTO = expenseBO.searchChart(searchDTO);

        assertThat(searchChartDTO.getData().size() == 0, is(false));
        assertThat(searchChartDTO.getData().get("Food"), is(500));
        assertThat(searchChartDTO.getData().get("Test"), is(1500));
        assertThat(searchChartDTO.getData().get("Car"), is(300));
    }
}
