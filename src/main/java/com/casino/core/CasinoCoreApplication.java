package com.casino.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CasinoCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(CasinoCoreApplication.class, args);
	}

}
