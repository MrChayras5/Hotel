package com.reserva.service;

import com.reserva.client.HabitacionClient;
import com.reserva.dto.HabitacionDTO;
import com.reserva.entity.Reserva;
import com.reserva.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

       
        HabitacionDTO habitacion = habitacionClient.obtenerHabitacion(reserva.getIdHabitacion());

        long noches = ChronoUnit.DAYS.between(reserva.getFechaEntrada(), reserva.getFechaSalida());
        reserva.setNoches((int) noches);

       
        double total = noches * habitacion.getPrecio();
        reserva.setTotal(total);

       
        reserva.setEstado("CONFIRMADA");

        return repository.save(reserva);
    }
}