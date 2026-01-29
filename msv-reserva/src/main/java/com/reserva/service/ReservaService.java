package com.reserva.service; // <--- CORREGIDO

import com.reserva.client.HabitacionClient; // <--- CORREGIDO
import com.reserva.dto.HabitacionDTO;       // <--- CORREGIDO
import com.reserva.entity.Reserva;          // <--- CORREGIDO
import com.reserva.repository.ReservaRepository; // <--- CORREGIDO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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



        //Quitar comentario
        //HabitacionDTO habitacion = habitacionClient.obtenerHabitacion(reserva.getIdHabitacion());

        long noches = ChronoUnit.DAYS.between(reserva.getFechaEntrada(), reserva.getFechaSalida());
        reserva.setNoches((int) noches);

        double precioPorNoche = 100.00;
        double totalCalculado = noches * precioPorNoche;

        //Quitar comentario
        //double totalCalculado = noches * habitacion.getPrecio();
        //reserva.setTotal(BigDecimal.valueOf(totalCalculado));
        reserva.setEstado("CONFIRMADA");

        return repository.save(reserva);
    }

    public Reserva buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    // Modificación para ELIMINADO LÓGICO (Cancelar reserva)
    public void eliminarReserva(Long id) {
        Reserva reserva = repository.findById(id).orElse(null);

        if (reserva != null) {
            // Validar que no se cancele una reserva que ya pasó o está en curso
            if ("EN_CURSO".equals(reserva.getEstado()) || "FINALIZADA".equals(reserva.getEstado())) {
                throw new RuntimeException("No se puede cancelar una reserva en curso o finalizada");
            }

            // En lugar de borrar, cambiamos el estado
            reserva.setEstado("CANCELADA");
            repository.save(reserva);
        }
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

                double totalCalculado = noches * habitacion.getPrecio();
                reservaExistente.setTotal(BigDecimal.valueOf(totalCalculado));
            }
            return repository.save(reservaExistente);
        }
        return null;
    }
}