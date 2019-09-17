package com.antilamer.thingTracker;

import com.antilamer.thingTracker.dto.ExpenseDTO;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.model.ExpenseTypeDictEntity;
import com.antilamer.thingTracker.repository.ExpenseRepo;
import com.antilamer.thingTracker.repository.ExpenseTypeDictRepo;
import com.antilamer.thingTracker.repository.GroupRepo;
import com.antilamer.thingTracker.repository.UserRepo;
import com.antilamer.thingTracker.service.AuthenticationBO;
import com.antilamer.thingTracker.service.ExpenseBO;
import com.antilamer.thingTracker.service.ExpenseBOImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExpenseBOImpl.class)
public class ExpenseBOTest {

    @Autowired
    private ExpenseBO expenseBO;

    @MockBean
    private ExpenseRepo expenseRepo;

    @MockBean
    private ExpenseTypeDictRepo expenseTypeDictRepo;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private GroupRepo groupRepo;

    @MockBean
    private AuthenticationBO authenticationBO;


    @Test
    public void whenCreateExpense_Valid() throws ValidationException {
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
    public void whenCreateExpense_ValidWithNoType() throws ValidationException {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setPrice(70000);
        expenseDTO.setTypes(new ArrayList<>());
        expenseDTO.getTypes().add("Car");

        given(expenseTypeDictRepo.findByNameIgnoreCase(any())).willReturn(Optional.empty());
        given(expenseTypeDictRepo.save(any())).willAnswer(i -> i.getArguments()[0]);

        expenseBO.createExpense(expenseDTO);
    }

    @Test(expected = ValidationException.class)
    public void whenCreateExpense_InvalidWithNoPrice() throws ValidationException {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setTypes(new ArrayList<>());
        expenseDTO.getTypes().add("Car");

        expenseBO.createExpense(expenseDTO);
    }

    @Test(expected = ValidationException.class)
    public void whenCreateExpense_InvalidWithNoType() throws ValidationException {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setPrice(70000);

        expenseBO.createExpense(expenseDTO);
    }


    @Test()
    public void whenSearchExpenseTypes_Valid() throws ValidationException {
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
        entity.setName("Food");
        entity.setUsedCount(100);
        expenseTypeDictEntity.add(entity);

        entity = new ExpenseTypeDictEntity();
        entity.setName("Test");
        entity.setUsedCount(0);
        expenseTypeDictEntity.add(entity);

        entity = new ExpenseTypeDictEntity();
        entity.setName("Car");
        entity.setUsedCount(25);
        expenseTypeDictEntity.add(entity);

        return expenseTypeDictEntity;
    }
}
