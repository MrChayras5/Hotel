package com.reserva.mapper;

import org.springframework.stereotype.Component;

import com.reserva.entity.Reserva;
import com.reservas_commons.dto.ReservaRequest;
import com.reservas_commons.dto.ReservaResponse;
import com.reservas_commons.mappers.CommoMapper;

@Component
public class ReservaMapper implements CommoMapper<ReservaRequest, ReservaResponse, Reserva>{

	@Override
	public ReservaResponse entityToResponce(Reserva entity) {
		if(entity==null) return null;
		
		return new ReservaResponse(
				entity.getIdReserva(),
				entity.getFechaEntrada(),
				entity.getFechaSalida(),
				entity.getNoches(),
				entity.getTotal(),
				entity.getEstado()
				);
	}

	@Override
	public Reserva requestToEntity(ReservaRequest request) {
		if(request==null) return null;
		
		Reserva reserva = new Reserva();
		
		reserva.setFechaEntrada(request.fechaEntrada());
		reserva.setFechaSalida(request.fechaSalida());
		//reserva.setNoches(request.);
		reserva.setTotal(request.total());
		reserva.getEstado();
		
		return reserva;
	}

	@Override
	public Reserva updateEntityFromRequest(ReservaRequest request, Reserva entity) {
		if(request==null) return null;
		
		entity.setFechaEntrada(request.fechaEntrada());
		entity.setFechaSalida(request.fechaSalida());
		//entity.setNoches(request.noches());
		entity.setTotal(request.total());
		entity.setEstado(request.estado());

		return entity;
	}

}
