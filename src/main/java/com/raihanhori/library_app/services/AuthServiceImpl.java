package com.raihanhori.library_app.services;

import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.raihanhori.library_app.entity.Role;
import com.raihanhori.library_app.entity.User;
import com.raihanhori.library_app.helper.ValidationHelper;
import com.raihanhori.library_app.repositories.UserRepository;
import com.raihanhori.library_app.requests.LoginRequest;
import com.raihanhori.library_app.requests.RegisterRequest;
import com.raihanhori.library_app.resources.AuthResource;
import com.raihanhori.library_app.security.JwtUtils;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ValidationHelper validationHelper;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	public AuthResource register(RegisterRequest request) {
		validationHelper.validate(request);
		
		User user = userRepository.findFirstByEmail(request.getEmail()).orElse(null);
		if (Objects.nonNull(user)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
		}
		
		user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(Role.USER);
		userRepository.save(user);
		
		String token = jwtUtils.generateToken(user);
		Date expiredAt = jwtUtils.getTokenExpiredAt(token);
		
		return AuthResource.builder()
				.token(token)
				.expiredAt(expiredAt.toInstant())
				.build();
	}

	@Override
	public AuthResource login(LoginRequest request) {
		validationHelper.validate(request);
		
		UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
		
		try {
			authenticationManager.authenticate(credentials);
		} catch (AuthenticationException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials");
		}
		
		User user = userRepository.findFirstByEmail(request.getEmail())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials"));
		
		String token = jwtUtils.generateToken(user);
		Date expiredAt = jwtUtils.getTokenExpiredAt(token);
		
		return AuthResource.builder().token(token).expiredAt(expiredAt.toInstant()).build();
	}

}
