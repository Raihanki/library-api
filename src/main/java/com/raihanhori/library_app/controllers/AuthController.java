package com.raihanhori.library_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.raihanhori.library_app.helper.SuccessResponseHelper;
import com.raihanhori.library_app.requests.LoginRequest;
import com.raihanhori.library_app.requests.RegisterRequest;
import com.raihanhori.library_app.resources.AuthResource;
import com.raihanhori.library_app.services.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.CREATED)
	SuccessResponseHelper<AuthResource> register(@RequestBody RegisterRequest request) {
		AuthResource auth = authService.register(request);
		
		return SuccessResponseHelper.<AuthResource>builder()
				.status(201)
				.data(auth)
				.build();
	}
	
	@PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.OK)
	SuccessResponseHelper<AuthResource> login(@RequestBody LoginRequest request) {
		AuthResource auth = authService.login(request);
		
		return SuccessResponseHelper.<AuthResource>builder()
				.status(200)
				.data(auth)
				.build();
	}
	
}
