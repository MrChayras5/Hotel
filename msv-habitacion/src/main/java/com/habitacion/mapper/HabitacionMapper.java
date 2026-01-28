package com.habitacion.mapper;

import org.springframework.stereotype.Component;

import com.habitacion.entity.Habitacion;
import com.reservas_commons.dto.HabitacionRequest;
import com.reservas_commons.dto.HabitacionResponse;
import com.reservas_commons.mappers.CommoMapper;

@Component
public class HabitacionMapper implements CommoMapper<HabitacionRequest, HabitacionResponse, Habitacion>{

	@Override
	public HabitacionResponse entityToResponce(Habitacion entity) {
		if (entity == null) return null;
		
		return new HabitacionResponse(
				entity.getIdHabitacion(),
				entity.getNumero(),
				entity.getTipo(),
				entity.getDescripcion(),
				entity.getPrecio(),
				entity.getCapacidad(),
				entity.getEstado()
				);
	}

	@Override
	public Habitacion requestToEntity(HabitacionRequest request) {
		if (request == null) return null;
		
		Habitacion habitacion = new Habitacion();
		
		habitacion.setNumero(request.numero());
		habitacion.setTipo(request.tipo());
		habitacion.setDescripcion(request.descripcion());
		habitacion.setPrecio(request.precio());
		habitacion.setCapacidad(request.capacidad());
		habitacion.setEstado(request.estado());
		
		return habitacion;
		

		
	}

	@Override
	public Habitacion updateEntityFromRequest(HabitacionRequest request, Habitacion entity) {
		entity.setTipo(request.tipo());
		entity.setDescripcion(request.descripcion());
		entity.setPrecio(request.precio());
		entity.setCapacidad(request.capacidad());
		entity.setEstado(request.estado());
		
		return entity;
	}
	
	

}
