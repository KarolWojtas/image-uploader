package com.karol.services;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.util.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestReporter;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.security.core.authority.AuthorityUtils;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.*;
import com.karol.domain.CustomUserDetails;
import com.karol.domain.UserDetailsDTO;
import com.karol.domain.mappers.UserDetailsMapper;

class UserDetailsServiceImplTest {
	@Mock
	UserDetailsRepository userRepository;
	UserDetailsMapper mapper;
	UserDetailsService userService;
	CustomUserDetails userDetails1;
	CustomUserDetails userDetails2;
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mapper = UserDetailsMapper.INSTANCE;
		
		userService = new UserDetailsServiceImpl(userRepository, mapper);
		
		userDetails1 = CustomUserDetails.builder().id(1l).username("username").firstName("firstName")
						.password("password").role("ROLE_USER,ROLE_ADMIN").build();
		userDetails2 = CustomUserDetails.builder().id(2l).username("username2").firstName("firstName2")
				.password("password2").role("ROLE_USER").email("e@ma.il").build();
	}

	@Test
	@DisplayName("Test finding given user and conversion to userDto")
	void testFindDtoByUsername(TestReporter reporter) {
		given(userRepository.findByUsername(Mockito.anyString())).willReturn(userDetails1);
		
		UserDetailsDTO userDto = userService.findDtoByUsername("username");
	
		assertNotNull(userDto);
		assertAll(	
				()-> assertEquals(this.userDetails1.getFirstName(), userDto.getFirstName() ),
				()-> assertIterableEquals(this.userDetails1.getAuthorities(), userDto.getAuthorities()),
				()->assertEquals(this.userDetails1.getUsername(), userDto.getUsername())
				);
		verify(this.userRepository, VerificationModeFactory.times(1)).findByUsername(anyString());
	}

	@Test
	@DisplayName("test find all users")
	void testFindAll() {
		given(userRepository.findAll()).willReturn(Arrays.asList(this.userDetails1,this.userDetails2));
		
		List<UserDetailsDTO> dtoList = this.userService.findAll();
		
		assertEquals(2, dtoList.size());
	}

	@Test
	void testFindByUsername() {
		given(userRepository.findByUsername(anyString())).willReturn(userDetails1);
		
		CustomUserDetails userDetailsFound = this.userService.findByUsername("u");
		
		assertEquals(userDetails1.getUsername(), userDetailsFound.getUsername());
		assertIterableEquals(this.userDetails1.getAuthorities(), userDetailsFound.getAuthorities());
		verify(userRepository, times(1)).findByUsername(anyString());
	}

	@Test
	void testIsUsernameAvailable() {
		given(userRepository.findByUsername(anyString())).willReturn(userDetails1);
		
		boolean isAvailableUsername = this.userService.isUsernameAvailable(userDetails1.getUsername());
		assertTrue(!isAvailableUsername);
		
	}
	@TestFactory
	Stream<DynamicTest> testIsUsernameAvailableFactory(){
		return Arrays.asList("username.", userDetails2.getUsername(), "uSername").stream()
				.map(username -> DynamicTest.dynamicTest("test for "+username, ()->{
					given(userRepository.findByUsername(anyString())).willReturn(userDetails1);
					
					boolean isAvailableUsername = this.userService.isUsernameAvailable(username);
					assertTrue(isAvailableUsername);
				}));
	}
	@TestFactory
	@DisplayName("test factory for returning dtos from username")
	Stream<DynamicTest> findDtoByUsernameFactory(){
		return Arrays.asList(this.userDetails1,this.userDetails2).stream()
			.map(user -> DynamicTest.dynamicTest("test for "+user, () -> {
				given(userRepository.findByUsername(Mockito.anyString())).willReturn(user);
				
				UserDetailsDTO userDto = userService.findDtoByUsername("username");
				assertNotNull(userDto);
				assertAll(	
						()-> assertEquals(user.getFirstName(), userDto.getFirstName() ),
						()-> assertIterableEquals(user.getAuthorities(), userDto.getAuthorities()),
						()->assertEquals(user.getUsername(), userDto.getUsername())
						);
				
			}));
	}
	

}
