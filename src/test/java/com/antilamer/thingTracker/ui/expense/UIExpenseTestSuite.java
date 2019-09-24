package com.antilamer.thingTracker.ui.expense;

import com.antilamer.thingTracker.ui.UIAuthTest;
import com.antilamer.thingTracker.ui.UITestConfiguration;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Suite.class)
@SpringBootTest(
        classes = {
                UITestConfiguration.class
        },
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc(secure = false, addFilters = false)
@Suite.SuiteClasses(value = {
        UIAuthTest.class,
        UIExpenseTest.class
})
public class UIExpenseTestSuite {

}
