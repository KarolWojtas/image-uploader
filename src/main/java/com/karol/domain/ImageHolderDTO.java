package com.karol.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;

@Data
public class ImageHolderDTO {
	private Long id;
	private String description;
	private int width;
	private int height;
	private String username;
	private boolean isPublic;
	private LocalDateTime timestamp;
	private List<Link> links = new ArrayList<Link>();
	
	public ImageHolderDTO addLink(Link ...links) {
		Arrays.asList(links).forEach(this.links::add);
		return this;
	}
}
