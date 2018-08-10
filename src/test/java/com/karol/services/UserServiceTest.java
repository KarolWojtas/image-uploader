package com.karol.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.karol.domain.CustomUserDetails;
import com.karol.domain.mappers.UserDetailsMapper;

class UserServiceTest {
	@Mock
	UserDetailsRepository userRepository;
	UserDetailsService userService;
	CustomUserDetails user;
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		userService = new UserDetailsServiceImpl(userRepository, UserDetailsMapper.INSTANCE);
		this.user = new CustomUserDetails();
		user.setId(1l);
		user.setUsername("henryk");
		user.setPassword("password");
	}

	@Test
	void testIsUsernameAvailable() {
		when(userRepository.findByUsername(Mockito.anyString())).thenReturn(null);
		
		assertTrue(userService.isUsernameAvailable("new"));
	
	}

}
