package com.example.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class AsdUserManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsdUserManagerApplication.class, args);
	}

}
