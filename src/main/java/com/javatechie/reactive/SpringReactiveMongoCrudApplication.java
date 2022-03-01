package com.javatechie.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "Spring Reactive programming with MongoDB", 
		version = "2.0", 
		description = "This is a demo for a Spring Reactive programming with MongoDB application"))
public class SpringReactiveMongoCrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringReactiveMongoCrudApplication.class, args);
	}

}
