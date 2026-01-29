package com.reserva.repository; // <--- CORREGIDO

import com.reserva.entity.Reserva; // <--- CORREGIDO
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}