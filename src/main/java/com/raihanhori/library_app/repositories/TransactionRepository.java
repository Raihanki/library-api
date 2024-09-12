package com.raihanhori.library_app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raihanhori.library_app.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

	Optional<Transaction> findByUserIdAndBookId(Integer userId, Integer bookId);
	
	List<Transaction> findAllByBookIdOrderByCreatedAtDesc(Integer bookId);
	
	List<Transaction> findAllByUserIdOrderByCreatedAtDesc(Integer userId);
	
}
