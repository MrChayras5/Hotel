package com.pavel.habitaciones.repository;

import com.pavel.habitaciones.entity.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {
    
    List<Habitacion> findByEstado(String estado);
}