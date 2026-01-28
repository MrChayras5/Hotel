package com.habitacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.habitacion","com.reservas_commons"})
public class MsvHabitacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvHabitacionApplication.class, args);
	}

}
