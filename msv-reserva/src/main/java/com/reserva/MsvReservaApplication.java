package com.reserva;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.reserva","com.reservas_commons"})
public class MsvReservaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvReservaApplication.class, args);
	}

}
