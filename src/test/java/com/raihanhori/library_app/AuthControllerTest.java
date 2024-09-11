package com.raihanhori.library_app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raihanhori.library_app.entity.Role;
import com.raihanhori.library_app.entity.User;
import com.raihanhori.library_app.helper.SuccessResponseHelper;
import com.raihanhori.library_app.repositories.UserRepository;
import com.raihanhori.library_app.requests.LoginRequest;
import com.raihanhori.library_app.requests.RegisterRequest;
import com.raihanhori.library_app.resources.AuthResource;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@BeforeEach
	void setUp() {
		userRepository.deleteAll();
	}
	
	@AfterEach
	void tearDown() {
		userRepository.deleteAll();
	}
	
	@Test
	void testRegisterSuccess() throws Exception {
		RegisterRequest request = new RegisterRequest();
		request.setName("Test");
		request.setEmail("test@test.com");
		request.setPassword("password");
		
		mockMvc.perform(
				post("/api/v1/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
		).andExpect(status().isCreated())
		.andDo(result -> {
			SuccessResponseHelper<AuthResource> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
			assertNotNull(response.getData().getToken());
			
			User createdUser = userRepository.findFirstByEmail(request.getEmail()).orElse(null);
			assertNotNull(createdUser);
			
			assertEquals(request.getName(), createdUser.getName());
			assertEquals(request.getEmail(), createdUser.getEmail());
		});
	}
	
	@Test
	void testLoginSuccess() throws Exception {
		User user = new User();
		user.setEmail("test@test.com");
		user.setName("Test");
		user.setPassword(passwordEncoder.encode("password"));
		user.setRole(Role.USER);
		userRepository.save(user);
		
		LoginRequest request = new LoginRequest();
		request.setEmail("test@test.com");
		request.setPassword("password");
		
		mockMvc.perform(
				post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
		).andExpect(status().isOk())
		.andDo(result -> {
			SuccessResponseHelper<AuthResource> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
			assertNotNull(response.getData().getToken());
		});
	}
}
