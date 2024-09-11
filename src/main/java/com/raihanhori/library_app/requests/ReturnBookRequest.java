package com.raihanhori.library_app.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnBookRequest {
	
	private Integer userId;
	
	private Integer bookId;
	
}
