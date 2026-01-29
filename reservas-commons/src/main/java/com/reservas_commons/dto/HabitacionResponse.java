package com.reservas_commons.dto;

import java.math.BigDecimal;


public record HabitacionResponse(
		Long idHabitacion,
		Integer numero,
		String tipo,
		String descripcion,
		BigDecimal precio,
		Integer capacidad,
		String estado
		
		){}
