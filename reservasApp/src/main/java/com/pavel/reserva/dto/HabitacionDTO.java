package com.pavel.reserva.dto;

public class HabitacionDTO {
    private Long id;
    private Double precio;
    private String estado;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}