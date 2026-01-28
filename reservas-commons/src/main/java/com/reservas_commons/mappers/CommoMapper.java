package com.reservas_commons.mappers;

public interface CommoMapper<RQ, RS, E> {

	RS entityToResponce(E entity);
	
	E requestToEntity(RQ request);
	
	E updateEntityFromRequest(RQ request, E entity);
}
