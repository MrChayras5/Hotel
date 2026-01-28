package com.huesped;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.huesped","com.reservas_commons"})
public class MsvHuespedApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvHuespedApplication.class, args);
	}

}
