package com.example.keyword;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class KeywordApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeywordApplication.class, args);
	}

}
