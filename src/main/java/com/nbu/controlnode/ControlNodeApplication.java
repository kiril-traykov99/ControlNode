package com.nbu.controlnode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ControlNodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ControlNodeApplication.class, args);
	}

}
