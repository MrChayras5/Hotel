package com.pavel.huespedes.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "HUESPEDES")
@Data
public class Huesped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HUESPED")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @Column(name = "APELLIDO", nullable = false, length = 100)
    private String apellido;

    @Column(name = "EMAIL", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "TELEFONO", nullable = false, length = 15, unique = true)
    private String telefono;

    @Column(name = "DOCUMENTO", nullable = false, length = 20, unique = true)
    private String documento;

    @Column(name = "NACIONALIDAD", nullable = false, length = 50)
    private String nacionalidad;
    
    @Column(name = "activo")
    private Boolean activo = true;

}