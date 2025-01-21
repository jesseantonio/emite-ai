package com.emiteai.emite_ai_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EmiteAiBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmiteAiBackendApplication.class, args);
	}

}
