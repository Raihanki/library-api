package com.raihanhori.library_app.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponseHelper<T> {
	
	private Integer status;
	
	private String message;

	private T data;
	
	
}
