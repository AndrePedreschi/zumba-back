package com.example.zumbasquad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@EnableAutoConfiguration
@SpringBootApplication
public class ZumbaSquadApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZumbaSquadApplication.class, args);
	}

}
