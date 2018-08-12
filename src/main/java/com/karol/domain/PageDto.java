package com.karol.domain;

import java.util.Iterator;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageDto <T> {
	private List<T> content;
	private Pageable pageable;
	private int number;
	private int numberOfELements;
	private Sort sort;
	private int totalPages;
	private long totalElements;
	@JsonProperty("last")
	private boolean isLast;
	@JsonProperty("first")
	private boolean isFirst;
	private int size;
	private List<Link> links;
	
}
