package com.federico.LessonBookingSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.federico.LessonBookingSystem.runners;com.federico.LessonBookingSystem.adapters;com.federico.LessonBookingSystem.application.usecases")
@SpringBootApplication
public class LessonBookingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(LessonBookingSystemApplication.class, args);
	}

}
