package com.reserva.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.reserva.entity.Reserva;
import com.reserva.mapper.ReservaMapper;
import com.reserva.repository.ReservaRepository;
import com.reservas_commons.dto.ReservaRequest;
import com.reservas_commons.dto.ReservaResponse;
import com.reservas_commons.enums.EstadoReserva;

import org.springframework.transaction.annotation.Transactional;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class ReservaServiceImp implements ReservaService{

    private final ReservaRepository reservaRepository;
    private final ReservaMapper reservaMapper;

	
	 @Override
	    @Transactional(readOnly = true)
	    public List<ReservaResponse> listar() {
	        
		 return reservaRepository
		            .findByEstadoNot(EstadoReserva.CANCELADA)
		            .stream()
		            .map(reservaMapper::entityToResponce)
		            .toList();
	    }

	    @Override
	    @Transactional(readOnly = true)
	    public ReservaResponse obtenerPorId(Long id) {
	        Reserva reserva = reservaRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
	        return reservaMapper.entityToResponce(reserva);
	    }

	    @Override
	    @Transactional
	    public ReservaResponse registrar(ReservaRequest request) {
	    
	        if (!request.fechaEntrada().isBefore(request.fechaSalida())) {
	            throw new RuntimeException("La fecha de entrada debe ser anterior a la de salida");
	        }

	       
	        int noches = (int) java.time.temporal.ChronoUnit.DAYS.between(request.fechaEntrada(), request.fechaSalida());
	        Reserva reserva = reservaMapper.requestToEntity(request);
	        reserva.setNoches(noches);
	        reserva.setEstado(EstadoReserva.CONFIRMADA);///-------------------


	        return reservaMapper.entityToResponce(reservaRepository.save(reserva));
	    }

	    @Override
	    @Transactional
	    public ReservaResponse actualizar(ReservaRequest request, Long id) {
	        Reserva reservaExistente = reservaRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

	        // Validar que solo se modifique si está Confirmada [cite: 262]
	        if (reservaExistente.getEstado() != EstadoReserva.CONFIRMADA) {
	            throw new RuntimeException(
	                "Solo se pueden modificar reservas en estado CONFIRMADA"
	            );
	        }
	        return reservaMapper.entityToResponce(reservaRepository.save(reservaExistente));
	    }

	    @Override
	    @Transactional
	    public void eliminar(Long id) {

	        Reserva reserva = reservaRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

	        if (reserva.getEstado() == EstadoReserva.EN_CURSO ||
	            reserva.getEstado() == EstadoReserva.FINALIZADA) {
	            throw new RuntimeException(
	                "No se puede cancelar una reserva en curso o finalizada"
	            );
	        }

	        reserva.setEstado(EstadoReserva.CANCELADA);

	        reservaRepository.save(reserva);
	    }
	    
	   
}
