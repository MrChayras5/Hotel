package com.pavel.huespedes.service;

import com.pavel.huespedes.entity.Huesped;
import com.pavel.huespedes.repository.HuespedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class HuespedService {

    @Autowired
    private HuespedRepository repository;

    @Transactional(readOnly = true)
    public List<Huesped> listar() {
        return repository.findAll();
    }

    @Transactional
    public Huesped guardar(Huesped huesped) {
        return repository.save(huesped);
    }

    @Transactional
    public Huesped actualizar(Huesped huesped, Long id) {
        // Validación simple para asegurar que existe
        return repository.findById(id).map(h -> {
            h.setNombre(huesped.getNombre());
            h.setApellido(huesped.getApellido());
            h.setEmail(huesped.getEmail());
            h.setTelefono(huesped.getTelefono());
            h.setDocumento(huesped.getDocumento());
            h.setNacionalidad(huesped.getNacionalidad());
            return repository.save(h);
        }).orElseThrow(() -> new RuntimeException("No existe el huesped"));
    }

    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}