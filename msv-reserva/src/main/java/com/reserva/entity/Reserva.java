package com.reserva.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "RESERVA") 
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RESERVA")
    private Long id;

    @Column(name = "FECHA_ENTRADA")
    private LocalDate fechaEntrada;

    @Column(name = "FECHA_SALIDA")
    private LocalDate fechaSalida;

    private Integer noches;
    private Double total;
    private String estado;

    @Column(name = "ID_HUESPED")
    private Long idHuesped;

    @Column(name = "ID_HABITACION")
    private Long idHabitacion;
    
    @Column(name = "ID_USUARIO")
    private Long idUsuario;

  
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(LocalDate fechaEntrada) { this.fechaEntrada = fechaEntrada; }

    public LocalDate getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(LocalDate fechaSalida) { this.fechaSalida = fechaSalida; }

    public Integer getNoches() { return noches; }
    public void setNoches(Integer noches) { this.noches = noches; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Long getIdHuesped() { return idHuesped; }
    public void setIdHuesped(Long idHuesped) { this.idHuesped = idHuesped; }

    public Long getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(Long idHabitacion) { this.idHabitacion = idHabitacion; }
    
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
}