package com.pavel.huespedes.repository;

import com.pavel.huespedes.entity.Huesped;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HuespedRepository extends JpaRepository<Huesped, Long> {
    // Aquí puedes agregar métodos extra como findByEmail si lo necesitas
}