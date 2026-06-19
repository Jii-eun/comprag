package com.proj.comprag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class CompragApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompragApplication.class, args);
	}

}
