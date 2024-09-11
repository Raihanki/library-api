package com.raihanhori.library_app.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTransactionRequest {

	@NotNull
	private Integer userId;
	
	@NotNull
	private Integer bookId;
	
}
