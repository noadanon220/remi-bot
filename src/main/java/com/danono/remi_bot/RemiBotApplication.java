package com.danono.remi_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RemiBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(RemiBotApplication.class, args);
	}

}
