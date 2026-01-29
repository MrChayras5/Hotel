package com.reservas_commons.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReservaRequest(
		
		@NotNull(message = "La fecha de entrada es requerida")
	    @FutureOrPresent(message = "La fecha de entrada debe ser hoy o una fecha futura")
	    LocalDate fechaEntrada,

	    @NotNull(message = "La fecha de salida es requerida")
	    @Future(message = "La fecha de salida debe ser estrictamente una fecha futura")
	    LocalDate fechaSalida,
	    @NotNull(message = "El número de noches es requerido")
	    @Positive(message = "El número de noches debe ser un número positivo")
	    Integer noches,
	    
	    @NotNull(message = "El total es requerido")
	    @Positive(message = "El total debe ser un número positivo")
	    BigDecimal total,
	    
	    @NotBlank(message = "El estado es requerido")
	    String estado

		) {

}
