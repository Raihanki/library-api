package com.raihanhori.library_app.security;

import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raihanhori.library_app.helper.ErrorResponseHelper;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationJwtFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserDetailsService userDetailService;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = jwtUtils.getTokenFromAuthorizationHeader(request);
		
		if (token == null) {
			filterChain.doFilter(request, response);
            return;
		}
		
		try {
			
			String email = jwtUtils.getUsernameFromToken(token);
			
			UserDetails userDetails = userDetailService.loadUserByUsername(email);
			if (Objects.nonNull(userDetails) && jwtUtils.isTokenValid(token, userDetails) && SecurityContextHolder.getContext().getAuthentication() == null) {
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
			
		} catch (JwtException e) {
			response.setStatus(401);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			
			ErrorResponseHelper error = ErrorResponseHelper.builder()
					.status(401)
					.message("unauthorized")
					.build();
			
			objectMapper.writeValue(response.getWriter(), error);
		}
		
		filterChain.doFilter(request, response);
		
	}

	
	
}
