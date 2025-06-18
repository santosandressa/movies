package com.cinema.plus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties
public class PlusApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlusApplication.class, args);
	}

}
