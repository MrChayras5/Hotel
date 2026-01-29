package com.pavel.reserva.controller;

import com.pavel.reserva.entity.Reserva;
import com.pavel.reserva.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reserva")
public class ReservaController {

    @Autowired
    private ReservaService service;

    @GetMapping
    public List<Reserva> listar() {
        return service.listarReservas();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Reserva reserva) {
        try {
            Reserva reservaGuardada = service.guardarReserva(reserva);
            return ResponseEntity.ok(reservaGuardada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // --- NUEVO: Obtener una reserva por ID ---
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerPorId(@PathVariable Long id) {
        Reserva reserva = service.buscarPorId(id);
        if (reserva != null) {
            return ResponseEntity.ok(reserva);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // --- NUEVO: Actualizar reserva ---
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Reserva reserva) {
        try {
            Reserva reservaActualizada = service.actualizarReserva(id, reserva);
            if (reservaActualizada != null) {
                return ResponseEntity.ok(reservaActualizada);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar: " + e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarReserva(id);
        return ResponseEntity.noContent().build();
    }
}