package com.karol.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.karol.domain.UserDetailsDTO;
import com.karol.domain.Views;
import com.karol.services.UserDetailsService;

@RestController
public class UserController {
	private UserDetailsService userService;
	@Autowired
	public UserController(UserDetailsService userService) {
		super();
		this.userService = userService;
	}
	@JsonView(Views.Private.class)
	@GetMapping("/users/me")
	public UserDetailsDTO getUserDetails() {
		return userService.findDtoByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
	}
	
}
