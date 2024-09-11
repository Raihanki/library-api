package com.raihanhori.library_app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.raihanhori.library_app.entity.Book;
import com.raihanhori.library_app.entity.Category;
import com.raihanhori.library_app.helper.ValidationHelper;
import com.raihanhori.library_app.repositories.BookRepository;
import com.raihanhori.library_app.repositories.CategoryRepository;
import com.raihanhori.library_app.requests.CreateBookRequest;
import com.raihanhori.library_app.requests.GetBookRequest;
import com.raihanhori.library_app.requests.UpdateBookRequest;
import com.raihanhori.library_app.resources.BookResource;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ValidationHelper validationHelper;
	
	@Override
	public void create(CreateBookRequest request) {
		validationHelper.validate(request);
		
		Category category = categoryRepository.findById(request.getCategory_id())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "category not found"));
	
		Book book = new Book();
		book.setTitle(request.getTitle());
		book.setAuthor(request.getAuthor());
		book.setPublisher(request.getPublisher());
		book.setPublishedAt(request.getPublishedAt().toInstant());
		book.setIsAvailable(request.getIsAvailable());
		book.setCategory(category);
		bookRepository.save(book);
	}

	@Override
	public void update(Integer id, UpdateBookRequest request) {
		validationHelper.validate(request);
		
		Category category = categoryRepository.findById(request.getCategory_id())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "category not found"));
		
		Book book = bookRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "book not found"));
		
		book.setTitle(request.getTitle());
		book.setAuthor(request.getAuthor());
		book.setPublisher(request.getPublisher());
		book.setPublishedAt(request.getPublishedAt());
		book.setIsAvailable(request.getIsAvailable());
		book.setCategory(category);	
		bookRepository.save(book);
	}

	@Override
	public BookResource getById(Integer id) {
		Book book = bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "book not found"));
		
		return this.toBookResource(book);
	}

	@Override
	public Page<BookResource> getAll(GetBookRequest request) {
		Pageable pageable = PageRequest.of(request.getPage() - 1, request.getLimit());
		
		Page<Book> books = bookRepository.findAllBookWithFilter(request.getSearch(), request.getCategory_id(), pageable);
		
		List<BookResource> listBook = books.stream().map(book -> 
			this.toBookResource(book)
		).toList();
		
		return new PageImpl<BookResource>(listBook, pageable, books.getTotalElements());
	}
	
	@Override
	public void destroy(Integer id) {
		Book book = bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "book not found"));
		
		bookRepository.delete(book);
	}
	
	private BookResource toBookResource(Book book) {
		return BookResource.builder().id(book.getId()).title(book.getTitle()).author(book.getAuthor())
				.publisher(book.getPublisher()).publishedAt(book.getPublishedAt()).isAvailable(book.getIsAvailable())
				.createdAt(book.getCreatedAt())
				.build();
	}

}
