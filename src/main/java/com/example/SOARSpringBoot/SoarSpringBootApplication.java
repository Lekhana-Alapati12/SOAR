package com.example.SOARSpringBoot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SoarSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoarSpringBootApplication.class, args);
	}

}
