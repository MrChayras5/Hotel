package com.pavel.auth.services;

import java.util.Set;

import com.pavel.auth.dto.UsuarioRequest;
import com.pavel.auth.dto.UsuarioResponse;

public interface UsuarioService {

	Set<UsuarioResponse> listar();

	UsuarioResponse registrar(UsuarioRequest request);

	UsuarioResponse eliminar(String username);

}
