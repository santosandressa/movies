package com.cinema.plus;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties
public class PlusApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlusApplication.class, args);
	}

	@Bean
	ApplicationRunner runner(PasswordEncoder passwordEncoder) {
		return args -> System.out.println(passwordEncoder.encode("password"));
	}

}
