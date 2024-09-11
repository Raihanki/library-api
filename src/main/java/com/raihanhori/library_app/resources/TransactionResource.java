package com.raihanhori.library_app.resources;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
public class TransactionResource {

	private Integer id;
	
	private UserResource user;
	
	private Integer bookId;
	
	private Instant createdAt;
	
	private Instant expiredAt;
	
	private Instant returnDate;
	
	private BigDecimal fineAmount;
	
}
