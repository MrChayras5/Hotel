package com.habitacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.habitacion.entity.Habitacion;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long>{
	boolean existsByNumero(Integer numero);
	
}
