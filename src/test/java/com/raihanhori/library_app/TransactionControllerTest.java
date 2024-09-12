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
import com.raihanhori.library_app.entity.Transaction;
import com.raihanhori.library_app.entity.User;
import com.raihanhori.library_app.helper.SuccessResponseHelper;
import com.raihanhori.library_app.repositories.BookRepository;
import com.raihanhori.library_app.repositories.CategoryRepository;
import com.raihanhori.library_app.repositories.TransactionRepository;
import com.raihanhori.library_app.repositories.UserRepository;
import com.raihanhori.library_app.requests.CreateTransactionRequest;
import com.raihanhori.library_app.requests.ReturnBookRequest;
import com.raihanhori.library_app.resources.TransactionResource;
import com.raihanhori.library_app.security.JwtUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.text.SimpleDateFormat;
import java.time.Instant;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	private User user;
	
	private String token;
	
	private Book book;
	
	@BeforeEach
	public void setup() throws Exception {
		transactionRepository.deleteAll();
		userRepository.deleteAll();
		bookRepository.deleteAll();
		
		user = new User();
		user.setName("Test");
		user.setEmail("test@gmail.com");
		user.setPassword(passwordEncoder.encode("password"));
		user.setRole(Role.ADMIN);
		userRepository.save(user);
		
		token = jwtUtils.generateToken(user);
		
		Category category = categoryRepository.findFirstByName("Science").orElse(null);
		assertNotNull(category);
		
		book = new Book();
		book.setTitle("Book 1");
		book.setAuthor("Author 1");
		book.setPublisher("Publisher 1");
		book.setPublishedAt(new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-01").toInstant());
		book.setIsAvailable(true);
		book.setCategory(category);
		bookRepository.save(book);
	}
	
	@AfterEach
	public void tearDown() {
		transactionRepository.deleteAll();
		userRepository.deleteAll();
		bookRepository.deleteAll();
	}
	
	@Test
	void testBorrowBook() throws Exception {
		CreateTransactionRequest request = new CreateTransactionRequest();
		request.setBookId(book.getId());
		request.setUserId(user.getId());
		
		mockMvc.perform(
				post("/api/v1/transactions")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.header("Authorization", "Bearer " + token)
		).andExpect(status().isCreated())
		.andDo(result -> {
			SuccessResponseHelper<Object> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
			assertNotNull(response);
			
			Transaction transaction = transactionRepository.findByUserIdAndBookId(request.getUserId(), request.getBookId()).orElse(null);
			assertNotNull(transaction);
			
			assertEquals(request.getUserId(), transaction.getUser().getId());
			assertEquals(request.getBookId(), transaction.getBook().getId());
			assertFalse(transaction.getBook().getIsAvailable());
		});
	}
	
	@Test
	void testReturnBook() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setBook(book);
        transaction.setUser(user);
        transaction.setExpiredAt(Instant.now().plusSeconds(60 * 60 * 24 * 7));
        transactionRepository.save(transaction);
        
        ReturnBookRequest request = new ReturnBookRequest();
        request.setUserId(user.getId());
        request.setBookId(book.getId());
        
        mockMvc.perform(
                put("/api/v1/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + token)
        ).andExpect(status().isOk())
        .andDo(result -> {
            SuccessResponseHelper<Object> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
            assertNotNull(response);
            
            Transaction updatedTransaction = transactionRepository.findByUserIdAndBookId(transaction.getUser().getId(), transaction.getBook().getId()).orElse(null);
			assertNotNull(transaction);
			
			assertNotNull(updatedTransaction.getReturnDate());
			
			assertTrue(updatedTransaction.getBook().getIsAvailable());
        });
	}
	
	@Test
	void testFindByTransactionId() throws Exception {
		Transaction transaction = new Transaction();
		transaction.setBook(book);
		transaction.setUser(user);
		transaction.setExpiredAt(Instant.now().plusSeconds(60 * 60 * 24 * 7));
		transactionRepository.save(transaction);

		mockMvc.perform(
				get("/api/v1/transactions/" + transaction.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
		).andExpect(status().isOk())
		.andDo(result -> {
			SuccessResponseHelper<TransactionResource> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
			assertNotNull(response);
			
			Transaction gettedTransaction = transactionRepository.findById(transaction.getId()).orElse(null);
			assertNotNull(gettedTransaction);
			
			assertEquals(transaction.getId(), gettedTransaction.getId());
			assertEquals(transaction.getBook().getId(), gettedTransaction.getBook().getId());
			assertEquals(transaction.getUser().getId(), gettedTransaction.getUser().getId());
			assertEquals(transaction.getExpiredAt(), gettedTransaction.getExpiredAt());
		});
	}
	
}
