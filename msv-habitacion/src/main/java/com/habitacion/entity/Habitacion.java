package com.habitacion.entity;

import java.math.BigDecimal;
import com.habitacion.enums.EstadoHabitacion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "HABITACIONES")
@NoArgsConstructor
@AllArgsConstructor
@Getter 
@Setter
@ToString
public class Habitacion {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HABITACION", nullable = false)
    private Long idHabitacion;

    @Column(name = "NUMERO", unique = true, nullable = false)
    private Integer numero;

    @Column(name = "TIPO", nullable = false, length = 20)
    private String tipo;

    @Column(name = "DESCRIPCION", length = 255)
    private String descripcion;

    @Column(name = "PRECIO", nullable = false)
    private BigDecimal precio;

    @Column(name = "CAPACIDAD", nullable = false)
    private Integer capacidad;

    @Column(name = "ESTADO", nullable = false, length = 20)
    @Enumerated(EnumType.STRING) // <--- ESTA LÍNEA ES LA QUE FALTA
    private EstadoHabitacion estado;
}