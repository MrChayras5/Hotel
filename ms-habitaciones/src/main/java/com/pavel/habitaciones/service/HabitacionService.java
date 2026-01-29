package com.pavel.habitaciones.service;

import com.pavel.habitaciones.entity.Habitacion;
import com.pavel.habitaciones.repository.HabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitacionService {

    @Autowired
    private HabitacionRepository repository;

    public List<Habitacion> listar() {
        return repository.findAll();
    }
    
    public List<Habitacion> listarDisponibles() {
        return repository.findByEstado("DISPONIBLE");
    }

    public Habitacion guardar(Habitacion habitacion) {
        
        if (habitacion.getEstado() == null) {
            habitacion.setEstado("DISPONIBLE");
        }
        return repository.save(habitacion);
    }

    public Habitacion buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Habitacion actualizarEstado(Long id, String nuevoEstado) {
        Habitacion habitacion = buscarPorId(id);
        if (habitacion != null) {
            habitacion.setEstado(nuevoEstado);
            return repository.save(habitacion);
        }
        return null;
    }
    
    public Habitacion actualizar(Long id, Habitacion datosNuevos) {
        Habitacion actual = buscarPorId(id);
        if (actual != null) {
            actual.setPrecio(datosNuevos.getPrecio());
            actual.setDescripcion(datosNuevos.getDescripcion());
            actual.setCapacidad(datosNuevos.getCapacidad());
            actual.setTipo(datosNuevos.getTipo());
            return repository.save(actual);
        }
        return null;
    }
}