package com.raihanhori.library_app;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raihanhori.library_app.entity.Book;
import com.raihanhori.library_app.entity.Category;
import com.raihanhori.library_app.entity.Role;
import com.raihanhori.library_app.entity.User;
import com.raihanhori.library_app.helper.SuccessResponseHelper;
import com.raihanhori.library_app.repositories.BookRepository;
import com.raihanhori.library_app.repositories.CategoryRepository;
import com.raihanhori.library_app.repositories.UserRepository;
import com.raihanhori.library_app.requests.CreateBookRequest;
import com.raihanhori.library_app.requests.UpdateBookRequest;
import com.raihanhori.library_app.resources.BookResource;
import com.raihanhori.library_app.security.JwtUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.text.SimpleDateFormat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	private User user;
	
	private String token;
	
	@BeforeEach
	public void setup() {
		userRepository.deleteAll();
		bookRepository.deleteAll();
		
		user = new User();
		user.setName("Test");
		user.setEmail("test@gmail.com");
		user.setPassword(passwordEncoder.encode("password"));
		user.setRole(Role.ADMIN);
		userRepository.save(user);
		
		token = jwtUtils.generateToken(user);
	}
	
	@AfterEach
	public void tearDown() {
		userRepository.deleteAll();
		bookRepository.deleteAll();
	}
	
	@Test
	void testCreateSuccess() throws Exception {
		Category category = categoryRepository.findFirstByName("Science").orElse(null);
		assertNotNull(category);
		
		CreateBookRequest request = new CreateBookRequest();
		request.setTitle("Book 1");
		request.setAuthor("Author 1");
		request.setPublisher("Publisher 1");
		request.setPublishedAt(new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-01"));
		request.setIsAvailable(true);
		request.setCategory_id(category.getId());
		
		mockMvc.perform(
				post("/api/v1/books")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(request))
				.header("Authorization", "Bearer " + token)
		).andExpect(status().isCreated())
		.andDo(result -> {
			SuccessResponseHelper<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
			assertNotNull(response);
			
			Book book = bookRepository.findFirstByTitle(request.getTitle()).orElse(null);
			assertNotNull(book);
			
			assertEquals(request.getTitle(), book.getTitle());
			assertEquals(request.getAuthor(), book.getAuthor());
			assertEquals(request.getPublisher(), book.getPublisher());
			assertEquals(request.getPublishedAt().toInstant(), book.getPublishedAt());
			assertEquals(request.getIsAvailable(), book.getIsAvailable());
			assertEquals(request.getCategory_id(), book.getCategory().getId());
		});
	}
	
	@Test
	void testGetByIdSuccess() throws Exception {
		Category category = categoryRepository.findFirstByName("Science").orElse(null);
		assertNotNull(category);
		
		Book book = new Book();
		book.setTitle("Book 1");
		book.setAuthor("Author 1");
		book.setPublisher("Publisher 1");
		book.setPublishedAt(new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-01").toInstant());
		book.setIsAvailable(true);
		book.setCategory(category);
		bookRepository.save(book);
		
		
		mockMvc.perform(
				get("/api/v1/books/" + book.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk())
		.andDo(result -> {
			SuccessResponseHelper<BookResource> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
			assertNotNull(response);
			
			Book gettedBook = bookRepository.findFirstByTitle(book.getTitle()).orElse(null);
			assertNotNull(book);
			
			assertEquals(gettedBook.getTitle(), book.getTitle());
			assertEquals(gettedBook.getAuthor(), book.getAuthor());
			assertEquals(gettedBook.getPublisher(), book.getPublisher());
			assertEquals(gettedBook.getPublishedAt(), book.getPublishedAt());
			assertEquals(gettedBook.getIsAvailable(), book.getIsAvailable());
			assertEquals(gettedBook.getCategory().getId(), book.getCategory().getId());
		});
	}
	
	@Test
	void testUpdateSuccess() throws Exception {
		Category category = categoryRepository.findFirstByName("Science").orElse(null);
		assertNotNull(category);
		
		Book book = new Book();
		book.setTitle("Book 1");
		book.setAuthor("Author 1");
		book.setPublisher("Publisher 1");
		book.setPublishedAt(new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-01").toInstant());
		book.setIsAvailable(true);
		book.setCategory(category);
		bookRepository.save(book);
		
		UpdateBookRequest request = new UpdateBookRequest();
		request.setTitle("Book 2");
		request.setAuthor("Author 2");
		request.setPublisher("Publisher 2");
		request.setPublishedAt(new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-02").toInstant());
		request.setIsAvailable(false);
		request.setCategory_id(category.getId());
		
		
		mockMvc.perform(
				put("/api/v1/books/" + book.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.header("Authorization", "Bearer " + token)
		).andExpect(status().isOk())
		.andDo(result -> {
			SuccessResponseHelper<Object> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
			assertNotNull(response);
			
			Book gettedBook = bookRepository.findFirstByTitle(request.getTitle()).orElse(null);
			Book oldBook = bookRepository.findFirstByTitle(book.getTitle()).orElse(null);
			assertNull(oldBook);
			assertNotNull(book);
			
			assertEquals(gettedBook.getTitle(), request.getTitle());
			assertEquals(gettedBook.getAuthor(), request.getAuthor());
			assertEquals(gettedBook.getPublisher(), request.getPublisher());
			assertEquals(gettedBook.getPublishedAt(), request.getPublishedAt());
			assertEquals(gettedBook.getIsAvailable(), request.getIsAvailable());
			assertEquals(gettedBook.getCategory().getId(), request.getCategory_id());
		});
	}
	
	@Test
	void testDeleteSuccess() throws Exception{
		Category category = categoryRepository.findFirstByName("Science").orElse(null);
		assertNotNull(category);
		
		Book book = new Book();
        book.setTitle("Book 1");
        book.setAuthor("Author 1");
        book.setPublisher("Publisher 1");
        book.setPublishedAt(new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-01").toInstant());
        book.setIsAvailable(true);
        book.setCategory(category);
        bookRepository.save(book);
        
        mockMvc.perform(
                delete("/api/v1/books/" + book.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
        ).andExpect(status().isOk())
        .andDo(result -> {
            Book deletedBook = bookRepository.findFirstByTitle(book.getTitle()).orElse(null);
            assertNull(deletedBook);
        });
    }
	
}
