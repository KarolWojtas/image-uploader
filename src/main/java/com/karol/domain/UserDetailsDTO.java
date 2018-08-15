package com.karol.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {
	
	private Long userId;
	@JsonView(Views.Public.class)
	private String firstName;
	@JsonView(Views.Public.class)
	private String lastName;
	@JsonView(Views.Public.class)
	private String username;
	//private String password;
	@JsonView(Views.Private.class)
	private String email;
	@JsonView(Views.Private.class)
	private List<? extends GrantedAuthority> authorities;
}
