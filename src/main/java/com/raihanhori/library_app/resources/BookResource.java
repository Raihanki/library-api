package com.raihanhori.library_app.resources;

import java.time.Instant;

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
public class BookResource {
	
	private Integer id;
	
	private String title;
	
	private String author;
	
	private String publisher;
	
	private Instant publishedAt;
	
	private Boolean isAvailable;
	
	private Instant createdAt;
	
	private CategoryResource category;
	
}
