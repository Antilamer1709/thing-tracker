package com.antilamer.thingTracker;

import com.antilamer.thingTracker.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class ThingTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThingTrackerApplication.class, args);
	}

}
