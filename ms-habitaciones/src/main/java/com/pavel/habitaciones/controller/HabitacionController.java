package com.pavel.habitaciones.controller;

import com.pavel.habitaciones.entity.Habitacion;
import com.pavel.habitaciones.service.HabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habitaciones")
public class HabitacionController {

    @Autowired
    private HabitacionService service;

    @GetMapping
    public List<Habitacion> listar() {
        return service.listar();
    }
    
    @GetMapping("/disponibles")
    public List<Habitacion> listarDisponibles() {
        return service.listarDisponibles();
    }

    @PostMapping
    public ResponseEntity<Habitacion> crear(@RequestBody Habitacion habitacion) {
        return ResponseEntity.ok(service.guardar(habitacion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Habitacion> obtener(@PathVariable Long id) {
        Habitacion hab = service.buscarPorId(id);
        return hab != null ? ResponseEntity.ok(hab) : ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Habitacion> actualizar(@PathVariable Long id, @RequestBody Habitacion habitacion) {
        Habitacion actualizada = service.actualizar(id, habitacion);
        return actualizada != null ? ResponseEntity.ok(actualizada) : ResponseEntity.notFound().build();
    }
}