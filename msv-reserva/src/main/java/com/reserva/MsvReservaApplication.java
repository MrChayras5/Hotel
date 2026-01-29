package com.reserva;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsvReservaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvReservaApplication.class, args);
	}

}