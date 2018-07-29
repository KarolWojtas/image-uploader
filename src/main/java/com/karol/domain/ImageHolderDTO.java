package com.karol.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ImageHolderDTO {
	private Long id;
	private String description;
	private int width;
	private int height;
	private boolean isPublic;
	private String imageUrl;
}
