package com.raihanhori.library_app.services;

import java.util.List;

import com.raihanhori.library_app.requests.CreateTransactionRequest;
import com.raihanhori.library_app.requests.ReturnBookRequest;
import com.raihanhori.library_app.resources.TransactionResource;

public interface TransactionService {
	
	void store(CreateTransactionRequest request);
	
	List<TransactionResource> findByBookId(Integer bookId);
	
	List<TransactionResource> findByUserId(Integer userId);
	
	void returnBook(ReturnBookRequest request);
	
	TransactionResource findById(Integer id);
	
}
