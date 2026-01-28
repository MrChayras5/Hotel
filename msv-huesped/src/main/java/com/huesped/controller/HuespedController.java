package com.huesped.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huesped.service.HuespedService;
import com.reservas_commons.controllers.CommonsController;
import com.reservas_commons.dto.HuespedRequest;
import com.reservas_commons.dto.HuespedResponse;


@RestController
public class HuespedController extends CommonsController<HuespedRequest, HuespedResponse, HuespedService>{

	public HuespedController(HuespedService service) {
		super(service);
		// TODO Auto-generated constructor stub
		
		
	}

}
