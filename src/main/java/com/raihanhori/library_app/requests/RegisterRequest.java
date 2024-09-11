package com.raihanhori.library_app.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
	
	@NotBlank
	@Size(max = 200)
	private String name;

	@NotBlank
	@Email
	@Size(max = 200)
	private String email;
	
	@NotBlank
	@Size(max = 200, min = 8)
	private String password;
	
}
