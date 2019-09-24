package com.antilamer.thingTracker.ui;

import org.junit.Assert;
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
public class UIAuthTest {

    @Value("${app.front.url}")
    private String frontUrl;

    @Autowired(required = true)
    RemoteWebDriver driver;

    @Test
    public void login() throws InterruptedException {
        driver.navigate().to(frontUrl + "/login");

        driver.findElementById("email").sendKeys("user1");
        driver.findElementById("password").sendKeys("user1");
        driver.findElementsByTagName("button").get(0).click();
        sleep(2500);

        Assert.assertTrue(driver.getCurrentUrl().endsWith("/main/dashboard"));
    }
}
