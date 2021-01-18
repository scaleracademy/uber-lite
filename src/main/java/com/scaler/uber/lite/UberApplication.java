package com.scaler.uber.lite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class UberApplication {

	public static void main(String[] args) {
		SpringApplication.run(UberApplication.class, args);
	}

}
