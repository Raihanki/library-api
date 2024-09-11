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
public class PaginationData {
	
	private Integer page;

	private Integer limit;

	private Long total;

	private Integer totalPages;

}
