package com.reserva.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.reservas_commons.enums.EstadoReserva;

@Entity
@Table(name = "RESERVA")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Reserva {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RESERVA")
    private Long idReserva;

    @NotNull(message = "La fecha de entrada es requerida")
    @FutureOrPresent(message = "La fecha de entrada debe ser hoy o en el futuro")
    @Column(name = "FECHA_ENTRADA", nullable = false)
    private LocalDate fechaEntrada;

    @NotNull(message = "La fecha de salida es requerida")
    @Future(message = "La fecha de salida debe ser una fecha futura")
    @Column(name = "FECHA_SALIDA", nullable = false)
    private LocalDate fechaSalida;

    @NotNull(message = "El número de noches es requerido")
    @Positive(message = "El número de noches debe ser mayor a cero")
    @Column(name = "NOCHES", nullable = false)
    private Integer noches;

    @NotNull(message = "El total es requerido")
    @Positive(message = "El total debe ser un monto positivo")
    @Column(name = "TOTAL", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false, length = 40)
    private EstadoReserva estado;
    

    @Column(name = "ID_HUESPED", nullable = false)
    private Long idHuesped;

    @Column(name = "ID_HABITACION", nullable = false)
    private Long idHabitacion;

}
