package com.huesped.mapper;

import org.springframework.stereotype.Component;

import com.huesped.entity.Huesped;
import com.reservas_commons.dto.HuespedRequest;
import com.reservas_commons.dto.HuespedResponse;
import com.reservas_commons.mappers.CommoMapper;

@Component
public class HuespedMapper implements CommoMapper<HuespedRequest, HuespedResponse, Huesped> {

	@Override
	public HuespedResponse entityToResponce(Huesped entity) {
		if(entity == null) return null;
		
		return new HuespedResponse(
				entity.getId(),
				entity.getNombre(),
				entity.getApellido(),
				entity.getEmail(),
				entity.getTelefono(),
				entity.getDocumento(),
				entity.getNacionalidad()
				);		
	}

	@Override
	public Huesped requestToEntity(HuespedRequest request) {
		if(request == null) return null;
		
		Huesped huesped = new Huesped();
		
		huesped.setNombre(request.nombre());
		huesped.setApellido(request.apellido());
		huesped.setEmail(request.email());
		huesped.setTelefono(request.telefono());
		huesped.setDocumento(request.documento());
		huesped.setNacionalidad(request.nacionalidad());
		
		return huesped;
	}

	@Override
	public Huesped updateEntityFromRequest(HuespedRequest request, Huesped entity) {
		entity.setNombre(request.nombre());
		entity.setApellido(request.apellido());
		entity.setEmail(request.email());
		entity.setTelefono(request.telefono());
		entity.setDocumento(request.documento());
		entity.setNacionalidad(request.nacionalidad());
		
		return entity;
	}

}
