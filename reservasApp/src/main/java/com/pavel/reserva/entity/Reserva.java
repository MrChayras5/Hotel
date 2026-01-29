package com.pavel.reserva.entity;

import jakarta.persistence.*;
import java.math.BigDecimal; // Importante para el dinero
import java.time.LocalDate;

@Entity
@Table(name = "RESERVA")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RESERVA")
    private Long id;

    @Column(name = "FECHA_ENTRADA", nullable = false)
    private LocalDate fechaEntrada;

    @Column(name = "FECHA_SALIDA", nullable = false)
    private LocalDate fechaSalida;

    @Column(nullable = false)
    private Integer noches;
    // (Borré el segundo "noches" que tenías repetido aquí)

    @Column(nullable = false)
    private BigDecimal total;
    // (Borré el "Double total", nos quedamos con BigDecimal para que coincida con Oracle)

    @Column(nullable = false)
    private String estado;

    @Column(name = "ID_HUESPED", nullable = false)
    private Long idHuesped;

    @Column(name = "ID_HABITACION", nullable = false)
    private Long idHabitacion;

    @Column(name = "ID_USUARIO")
    private Long idUsuario;

    // --- GETTERS Y SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(LocalDate fechaEntrada) { this.fechaEntrada = fechaEntrada; }

    public LocalDate getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(LocalDate fechaSalida) { this.fechaSalida = fechaSalida; }

    public Integer getNoches() { return noches; }
    public void setNoches(Integer noches) { this.noches = noches; }

    // OJO AQUÍ: Actualicé estos dos para usar BigDecimal
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Long getIdHuesped() { return idHuesped; }
    public void setIdHuesped(Long idHuesped) { this.idHuesped = idHuesped; }

    public Long getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(Long idHabitacion) { this.idHabitacion = idHabitacion; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
}