package com.raihanhori.library_app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.raihanhori.library_app.helper.SuccessResponseHelper;
import com.raihanhori.library_app.requests.CreateTransactionRequest;
import com.raihanhori.library_app.requests.ReturnBookRequest;
import com.raihanhori.library_app.resources.TransactionResource;
import com.raihanhori.library_app.services.TransactionService;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	
	@GetMapping(path = "/book/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.OK)
	SuccessResponseHelper<List<TransactionResource>> getAllByBookId(@PathVariable Integer bookId) {
		List<TransactionResource> response = transactionService.findByBookId(bookId);
		
		return SuccessResponseHelper.<List<TransactionResource>>builder()
				.data(response)
				.status(200)
				.message("transaction list fetched successfully")
				.build();
	}
	
	@GetMapping(path = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.OK)
	SuccessResponseHelper<List<TransactionResource>> getAllByUserId(@PathVariable Integer userId) {
		List<TransactionResource> response = transactionService.findByBookId(userId);
		
		return SuccessResponseHelper.<List<TransactionResource>>builder()
				.data(response)
				.status(200)
				.message("transaction list fetched successfully")
				.build();
	}
	
	@GetMapping(path = "/{transactionId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.OK)
	SuccessResponseHelper<TransactionResource> getById(@PathVariable Integer transactionId) {
		TransactionResource response = transactionService.findById(transactionId);
		
		return SuccessResponseHelper.<TransactionResource>builder()
				.data(response)
				.status(200)
				.message("transaction fetched successfully")
				.build();
	}
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.CREATED)
	SuccessResponseHelper<Object> createTransaction(@RequestBody CreateTransactionRequest request) {
		transactionService.store(request);
		
		return SuccessResponseHelper.<Object>builder()
				.data(null)
				.status(201)
				.message("transaction list created successfully")
				.build();
	}
	
	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.OK)
	SuccessResponseHelper<Object> returnBook(@RequestBody ReturnBookRequest request) {
		transactionService.returnBook(request);
		
		return SuccessResponseHelper.<Object>builder()
				.data(null)
				.status(201)
				.message("book returned successfully")
				.build();
	}
	
}
