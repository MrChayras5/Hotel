package com.pavel.auth.services;

import com.pavel.auth.dto.LoginRequest;
import com.pavel.auth.dto.TokenResponse;

public interface AuthService {

	TokenResponse autenticar(LoginRequest request) throws Exception;

}
