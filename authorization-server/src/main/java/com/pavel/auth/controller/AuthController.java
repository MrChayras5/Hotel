package com.pavel.auth.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pavel.auth.dto.LoginRequest;
import com.pavel.auth.dto.TokenResponse;
import com.pavel.auth.dto.UsuarioRequest;
import com.pavel.auth.dto.UsuarioResponse;
import com.pavel.auth.services.AuthService;
import com.pavel.auth.services.UsuarioService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AuthController {

	private final AuthService authService;

	private final UsuarioService usuarioService;

	@PostMapping("/api/login")
	public ResponseEntity<TokenResponse> generarToken(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
		return ResponseEntity.ok(authService.autenticar(loginRequest));
	}

	@GetMapping("/admin/usuarios")
	public ResponseEntity<Set<UsuarioResponse>> listarUsuarios() {
		return ResponseEntity.ok(usuarioService.listar());
	}

	@PostMapping("/admin/usuarios")
	public ResponseEntity<UsuarioResponse> registrarUsuarios(@Valid @RequestBody UsuarioRequest request) {
		return ResponseEntity.ok(usuarioService.registrar(request));
	}

	@DeleteMapping("/admin/usuarios/{username}")
	public ResponseEntity<UsuarioResponse> eliminarUsuario(@PathVariable String username) {
		return ResponseEntity.ok(usuarioService.eliminar(username));
	}

}
