package com.raihanhori.library_app.requests;

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
public class GetBookRequest {

	private Integer limit;
	
	private Integer page;
	
	private String search;
	
	private Integer category_id;
	
}
