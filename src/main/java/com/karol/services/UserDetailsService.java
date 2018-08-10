package com.karol.services;

import java.util.List;

import com.karol.domain.CustomUserDetails;
import com.karol.domain.UserDetailsDTO;

public interface UserDetailsService{
	UserDetailsDTO findDtoByUsername(String username);
	CustomUserDetails findByUsername(String username);
	List<UserDetailsDTO> findAll();
	boolean isUsernameAvailable(String username);
	
}
