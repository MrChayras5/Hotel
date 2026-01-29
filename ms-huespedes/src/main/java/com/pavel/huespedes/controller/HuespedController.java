package com.pavel.huespedes.controller;

import com.pavel.huespedes.entity.Huesped;
import com.pavel.huespedes.service.HuespedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// @RequestMapping("/huespedes")  <--- BORRA O COMENTA ESTA LÍNEA
@RequestMapping("/") // <--- CAMBIALO POR ESTO (Raíz)
//@CrossOrigin(origins = "http://localhost:4200")
public class HuespedController {

    @Autowired
    private HuespedService service;
    
    // Ahora, cuando el Gateway mande "/", caerá aquí
    @GetMapping
    public List<Huesped> listar() {
        return service.listar();
    }

    @PostMapping
    public Huesped crear(@RequestBody Huesped huesped) {
        return service.guardar(huesped);
    }

    @PutMapping("/{id}")
    public Huesped editar(@RequestBody Huesped huesped, @PathVariable Long id) {
        return service.actualizar(huesped, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}