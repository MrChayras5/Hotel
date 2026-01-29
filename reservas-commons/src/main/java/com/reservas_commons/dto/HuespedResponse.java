package com.reservas_commons.dto;

public record HuespedResponse(
	    Long idHuesped,
	    String nombre,
	    String apellido,
	    String email,
	    String telefono,
	    String documento,
	    String nacionalidad
	) {}