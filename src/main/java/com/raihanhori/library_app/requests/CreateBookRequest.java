package com.raihanhori.library_app.requests;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookRequest {
	
	@NotBlank
	@Size(max = 200)
	private String title;
	
	@NotBlank
	@Size(max = 200)
	private String author;
	
	@NotBlank
	@Size(max = 200)
	private String publisher;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date publishedAt;
	
	@NotNull
	private Boolean isAvailable;
	
	@NotNull
	private Integer category_id;
	
}
