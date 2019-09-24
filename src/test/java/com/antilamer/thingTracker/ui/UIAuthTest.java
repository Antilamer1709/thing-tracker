package com.antilamer.thingTracker.ui;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static java.lang.Thread.sleep;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        classes = {
                UITestConfiguration.class
        },
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc(secure = false, addFilters = false)
public class UIAuthTest {

    @Value("${app.front.url}")
    private String frontUrl;

    @Autowired(required = true)
    RemoteWebDriver remoteWebDriver;

    @Test
    public void test() throws InterruptedException {
        remoteWebDriver.navigate().to(frontUrl + "/login");

        remoteWebDriver.findElementById("email").sendKeys("user1");
        remoteWebDriver.findElementById("password").sendKeys("user1");
        remoteWebDriver.findElementsByTagName("button").get(0).click();
        sleep(2500);

        Assert.assertTrue(remoteWebDriver.getCurrentUrl().endsWith("/main/dashboard"));
    }
}
