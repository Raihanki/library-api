package com.raihanhori.library_app.services;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.raihanhori.library_app.entity.Book;
import com.raihanhori.library_app.entity.Role;
import com.raihanhori.library_app.entity.Transaction;
import com.raihanhori.library_app.entity.User;
import com.raihanhori.library_app.helper.ValidationHelper;
import com.raihanhori.library_app.repositories.BookRepository;
import com.raihanhori.library_app.repositories.TransactionRepository;
import com.raihanhori.library_app.repositories.UserRepository;
import com.raihanhori.library_app.requests.CreateTransactionRequest;
import com.raihanhori.library_app.requests.ReturnBookRequest;
import com.raihanhori.library_app.resources.TransactionResource;
import com.raihanhori.library_app.resources.UserResource;

import jakarta.transaction.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private ValidationHelper validationHelper;

	@Transactional
	@Override
	public void store(CreateTransactionRequest request) {
		validationHelper.validate(request);
		
		User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
		Book book = bookRepository.findById(request.getBookId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "book not found"));
	
		if (book.getIsAvailable() == false) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "book is not available");
		}
		
		Transaction checkTransaction = transactionRepository.findByUserIdAndBookId(request.getUserId(), request.getBookId()).orElse(null);
		if (Objects.nonNull(checkTransaction) && checkTransaction.getReturnDate() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "transaction already exists");
		}
		
		book.setIsAvailable(false);
		bookRepository.save(book);
		
		Transaction transaction = new Transaction();
		transaction.setUser(user);
		transaction.setBook(book);
		transaction.setExpiredAt(Instant.now().plusSeconds(60 * 60 * 24 * 7));
		transaction.setFineAmount(new BigDecimal(0));
		transactionRepository.save(transaction);
	}

	@Override
	public List<TransactionResource> findByBookId(Integer bookId) {
		List<Transaction> transactions = transactionRepository.findAllByBookIdOrderByCreatedAtDesc(bookId);
		
		List<TransactionResource> listTransaction = transactions.stream().map(t -> 
			toTransactionResource(t)
		).toList();
		
		return listTransaction;
	}
	
	@Override
	public List<TransactionResource> findByUserId(Integer userId) {
		List<Transaction> transactions = transactionRepository.findAllByUserIdOrderByCreatedAtDesc(userId);

		List<TransactionResource> listTransaction = transactions.stream().map(t -> toTransactionResource(t)).toList();

		return listTransaction;
	}
	
	@Transactional
	@Override
	public void returnBook(ReturnBookRequest request) {
		validationHelper.validate(request);
		
		Book book = bookRepository.findById(request.getBookId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "book not found"));
		User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
		
		Transaction transaction = transactionRepository.findByUserIdAndBookId(user.getId(), book.getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "transaction not found"));
		
		// check fine charge
		if (transaction.getExpiredAt().isBefore(Instant.now())) {
			long diff = Instant.now().getEpochSecond() - transaction.getExpiredAt().getEpochSecond();
			long days = diff / (60 * 60 * 24);
			BigDecimal fine = new BigDecimal(days * 2000);
			transaction.setFineAmount(fine);
		}
		
		System.out.println("after check fine");
		
		transaction.setReturnDate(Instant.now());
		transactionRepository.save(transaction);
		
		book.setIsAvailable(true);
		bookRepository.save(book);
		
	}
	
	@Override
	public TransactionResource findById(Integer id) {
		Transaction transaction = transactionRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "transaction not found"));

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (user.getRole().equals(Role.USER)) {
			if (!user.getEmail().equals(transaction.getUser().getEmail())) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "you are not allowed to access this transaction");
			}
		}
		
		return toTransactionResource(transaction);
	}
	
	private TransactionResource toTransactionResource(Transaction transaction) {
		UserResource user = UserResource.builder()
				.id(transaction.getUser().getId())
				.name(transaction.getUser().getName())
				.email(transaction.getUser().getEmail())
				.build();
		
		return TransactionResource.builder()
				.id(transaction.getId())
				.bookId(transaction.getBook().getId())
				.user(user)
				.createdAt(transaction.getCreatedAt())
				.returnDate(transaction.getReturnDate())
				.expiredAt(transaction.getExpiredAt())
				.fineAmount(transaction.getFineAmount())
				.build();
	}
	
}
