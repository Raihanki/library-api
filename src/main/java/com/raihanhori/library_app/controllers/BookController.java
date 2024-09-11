package com.raihanhori.library_app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.raihanhori.library_app.helper.PaginationData;
import com.raihanhori.library_app.helper.PaginationResponseHelper;
import com.raihanhori.library_app.helper.SuccessResponseHelper;
import com.raihanhori.library_app.requests.CreateBookRequest;
import com.raihanhori.library_app.requests.GetBookRequest;
import com.raihanhori.library_app.requests.UpdateBookRequest;
import com.raihanhori.library_app.resources.BookResource;
import com.raihanhori.library_app.services.BookService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/v1/books")
public class BookController {

	@Autowired
	private BookService bookService;
	
	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.OK)
	PaginationResponseHelper<List<BookResource>> index(
			@RequestParam(defaultValue = "10", required = false) Integer limit,
			@RequestParam(defaultValue = "1", required = false) Integer page,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) Integer category_id
	) {
		GetBookRequest request = new GetBookRequest();
		request.setCategory_id(category_id);
		request.setLimit(limit);
		request.setPage(page);
		request.setSearch(search);
		
		Page<BookResource> response = bookService.getAll(request);
		
		PaginationData paginationData = PaginationData.builder()
				.limit(response.getSize())
				.page(page)
				.total(response.getTotalElements())
				.totalPages(response.getTotalPages())
				.build();
		
		return PaginationResponseHelper.<List<BookResource>>builder()
				.status(200)
				.message("books fetched successfully")
				.data(response.getContent())
				.options(paginationData)
				.build();
	}
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.CREATED)
	SuccessResponseHelper<Object> store(@RequestBody CreateBookRequest request) {
		bookService.create(request);
		return SuccessResponseHelper.builder()
				.status(201)
				.message("book created successfully")
				.data(null)
				.build();
	}
	
	@GetMapping(path = "/{book_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.OK)
	SuccessResponseHelper<BookResource> getById(@PathVariable Integer book_id) {
		BookResource response = bookService.getById(book_id);
		return SuccessResponseHelper.<BookResource>builder()
				.status(200)
				.message("book fetched successfully")
				.data(response)
				.build();
	}
	
	@PutMapping(path = "/{book_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.OK)
	SuccessResponseHelper<Object> update(@PathVariable Integer book_id, @RequestBody UpdateBookRequest request) {
		bookService.update(book_id, request);
		return SuccessResponseHelper.<Object>builder()
				.status(200)
				.message("book updated successfully")
				.data(null)
				.build();
	}
	
	@DeleteMapping(path = "/{book_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.OK)
	SuccessResponseHelper<Object> delete(@PathVariable Integer book_id) {
		bookService.destroy(book_id);
		return SuccessResponseHelper.<Object>builder()
				.status(200)
				.message("book deleted successfully")
				.data(null)
				.build();
	}
	
}
