package com.antilamer.thingTracker.ui;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

@TestConfiguration
@PropertySource("classpath:application-test.properties")
public class UITestConfiguration {
    @Bean()
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public RemoteWebDriver remoteWebDriver() {
        System.setProperty(
                "webdriver.chrome.driver",
                "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        return new ChromeDriver();
    }
}
