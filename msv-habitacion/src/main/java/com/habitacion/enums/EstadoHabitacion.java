package com.habitacion.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
 * 
 */

@Getter
@AllArgsConstructor
public enum EstadoHabitacion {
	
	DISPONIBLE(1L, "Disponible"),
	En_CONSULTAR(2L, "Para consultar"),
	OCUPADA(3L, "Ocupado"),
	LIMPIEZA(4L, "Limpieza"),
	MANTENIMIENTO(5L, "Mantenimiento");
	
private final Long codigo;
	
	private final String descripcion;
	
	public static EstadoHabitacion fromCodigo(Long codigo) {
        for (EstadoHabitacion estado : EstadoHabitacion.values()) {
            if (estado.getCodigo() == codigo) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Código de disponibilidad no válido: " + codigo);
    }

}
