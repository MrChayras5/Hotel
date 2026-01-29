package com.reserva.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reserva.service.ReservaService;
import com.reservas_commons.controllers.CommonsController;
import com.reservas_commons.dto.ReservaRequest;
import com.reservas_commons.dto.ReservaResponse;

@RestController
public class ReservaController extends CommonsController<ReservaRequest, ReservaResponse, ReservaService>{

	public ReservaController(ReservaService service) {
		super(service);
		// TODO Auto-generated constructor stub
	}
	
	

}
