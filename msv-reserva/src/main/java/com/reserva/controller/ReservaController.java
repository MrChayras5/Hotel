package com.reserva.controller;

import com.reserva.entity.Reserva;
import com.reserva.service.ReservaService;
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
}