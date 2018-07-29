package com.karol.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity 
@Data
public class ImageHolder {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name="user_id")
	private CustomUserDetails user;
	private LocalDateTime timestamp = LocalDateTime.now();
	private String description;
	@Lob
	private byte[] image;
	
}
