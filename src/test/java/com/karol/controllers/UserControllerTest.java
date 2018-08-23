package com.karol.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import static org.mockito.BDDMockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import com.karol.domain.UserDetailsDTO;
import com.karol.services.UserDetailsService;
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
	@Autowired
	MockMvc mockMvc;
	@MockBean
	private UserDetailsService userService;
	@InjectMocks
	private UserController controller;
	private UserDetailsDTO userDto = new UserDetailsDTO(1l, "first", "last", "username", "e@ma.il", AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	@WithMockUser(username="user", password="password", roles= {"USER","ADMIN"})
	public void testGetUserDetails() throws Exception {
		given(userService.findDtoByUsername(Mockito.anyString())).willReturn(userDto);
		mockMvc.perform(get("/users/me"))	
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.username", is(equalTo(userDto.getUsername()))))
			.andExpect(jsonPath("$.firstName", is(equalTo(userDto.getFirstName()))))
			;
		
		verify(userService, times(1)).findDtoByUsername(Mockito.anyString());
		
	}

}
