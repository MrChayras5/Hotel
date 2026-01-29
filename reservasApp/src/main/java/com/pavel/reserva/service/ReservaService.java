package com.pavel.reserva.service;

import com.pavel.reserva.client.HabitacionClient;
import com.pavel.reserva.dto.HabitacionDTO;
import com.pavel.reserva.entity.Reserva;
import com.pavel.reserva.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal; // <--- IMPORTANTE: Agregado
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository repository;

    @Autowired
    private HabitacionClient habitacionClient;

    public List<Reserva> listarReservas() {
        return repository.findAll();
    }

    public Reserva guardarReserva(Reserva reserva) {

        if (reserva.getFechaSalida().isBefore(reserva.getFechaEntrada())) {
            throw new RuntimeException("La fecha de salida debe ser posterior a la entrada");
        }

        // Obtenemos precio del microservicio de habitaciones
        HabitacionDTO habitacion = habitacionClient.obtenerHabitacion(reserva.getIdHabitacion());

        // Calculamos noches
        long noches = ChronoUnit.DAYS.between(reserva.getFechaEntrada(), reserva.getFechaSalida());
        reserva.setNoches((int) noches);

        // --- CAMBIO AQUÍ: Convertimos a BigDecimal para la Base de Datos ---
        double totalCalculado = noches * habitacion.getPrecio();
        reserva.setTotal(BigDecimal.valueOf(totalCalculado));

        reserva.setEstado("CONFIRMADA");

        return repository.save(reserva);
    }

    public Reserva buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void eliminarReserva(Long id) {
        repository.deleteById(id);
    }

    public Reserva actualizarReserva(Long id, Reserva reservaActualizada) {
        Reserva reservaExistente = repository.findById(id).orElse(null);

        if (reservaExistente != null) {
            reservaExistente.setIdHuesped(reservaActualizada.getIdHuesped());
            reservaExistente.setIdHabitacion(reservaActualizada.getIdHabitacion());
            reservaExistente.setEstado(reservaActualizada.getEstado());

            boolean fechasCambiaron = !reservaExistente.getFechaEntrada().isEqual(reservaActualizada.getFechaEntrada()) ||
                    !reservaExistente.getFechaSalida().isEqual(reservaActualizada.getFechaSalida());

            if (fechasCambiaron) {
                reservaExistente.setFechaEntrada(reservaActualizada.getFechaEntrada());
                reservaExistente.setFechaSalida(reservaActualizada.getFechaSalida());

                if (reservaExistente.getFechaSalida().isBefore(reservaExistente.getFechaEntrada())) {
                    throw new RuntimeException("La fecha de salida debe ser posterior a la entrada");
                }

                HabitacionDTO habitacion = habitacionClient.obtenerHabitacion(reservaExistente.getIdHabitacion());

                long noches = ChronoUnit.DAYS.between(reservaExistente.getFechaEntrada(), reservaExistente.getFechaSalida());
                reservaExistente.setNoches((int) noches);

                // --- CAMBIO AQUÍ TAMBIÉN ---
                double totalCalculado = noches * habitacion.getPrecio();
                reservaExistente.setTotal(BigDecimal.valueOf(totalCalculado));
            }

            return repository.save(reservaExistente);
        }
        return null;
    }
}