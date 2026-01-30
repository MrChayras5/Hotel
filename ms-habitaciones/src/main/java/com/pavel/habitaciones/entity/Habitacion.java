package com.pavel.habitaciones.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "HABITACIONES")
@Data
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HABITACION")
    private Long id;

    @NotNull
    @Column(unique = true, nullable = false)
    private Integer numero;

    @NotNull
    private String tipo; 

    private String descripcion;

    @NotNull
    @Min(value = 0, message = "El precio debe ser positivo")
    private BigDecimal precio;

    @Min(value = 1, message = "Capacidad mínima de 1 persona")
    private Integer capacidad;

    @NotNull
    private String estado; 
}