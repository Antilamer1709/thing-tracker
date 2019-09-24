package com.antilamer.thingTracker.ui.expense;

import com.antilamer.thingTracker.ui.UITestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static java.lang.Thread.sleep;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        classes = {
                UITestConfiguration.class
        },
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UIExpenseTest {

    @Value("${app.front.url}")
    private String frontUrl;

    @Autowired()
    private RemoteWebDriver driver;

    @Test
    public void createExpense() throws InterruptedException {
        driver.navigate().to(frontUrl + "/main/add-expense");

        driver.findElementsByTagName("input").get(0).sendKeys("Food");
        driver.findElementsByTagName("input").get(1).sendKeys("350");
        driver.findElementById("submit").click();
        sleep(1500);
    }
}
