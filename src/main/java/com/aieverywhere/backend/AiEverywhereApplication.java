package com.aieverywhere.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AiEverywhereApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiEverywhereApplication.class, args);
	}

}
