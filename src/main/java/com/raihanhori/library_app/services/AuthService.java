package com.raihanhori.library_app.services;

import com.raihanhori.library_app.requests.LoginRequest;
import com.raihanhori.library_app.requests.RegisterRequest;
import com.raihanhori.library_app.resources.AuthResource;

public interface AuthService {
	
	AuthResource register(RegisterRequest request);
	
	AuthResource login(LoginRequest request);
	
}
