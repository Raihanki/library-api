package com.raihanhori.library_app.services;

import org.springframework.data.domain.Page;

import com.raihanhori.library_app.requests.CreateBookRequest;
import com.raihanhori.library_app.requests.GetBookRequest;
import com.raihanhori.library_app.requests.UpdateBookRequest;
import com.raihanhori.library_app.resources.BookResource;

public interface BookService {
	
	void create(CreateBookRequest request);
	
	void update(Integer id, UpdateBookRequest request);
	
	BookResource getById(Integer id);
	
	Page<BookResource> getAll(GetBookRequest request);
	
	void destroy(Integer id);
	
}
