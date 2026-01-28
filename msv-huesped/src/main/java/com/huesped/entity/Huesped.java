package com.huesped.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "HUESPED")
@NoArgsConstructor
@AllArgsConstructor
@Getter 
@Setter
@ToString
public class Huesped {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "ID_HUESPED")
	    private Long id;

	    @Column(name = "NOMBRE", nullable = false, length = 50)
	    private String nombre;

	    @Column(name = "APELLIDO",nullable = false, length = 50)
	    private String apellido;

	    @Column(name = "EMAIL",length = 100)
	    private String email;

	    @Column(name = "TELEFONO",length = 20)
	    private String telefono;

	    @Column(name = "DOCUMENTO",nullable = false, unique = true, length = 15)
	    private String documento;
	    
	    @Column(name = "NACIONALIDAD",nullable = false, unique = true, length = 15)
	    private String nacionalidad;
	
	

}
