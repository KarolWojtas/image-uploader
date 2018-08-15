package com.karol.services;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karol.domain.CustomUserDetails;
import com.karol.domain.UserDetailsDTO;
import com.karol.domain.mappers.UserDetailsMapper;
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	private UserDetailsRepository userRepository;
	private UserDetailsMapper userMapper;
	@Autowired
	public UserDetailsServiceImpl(UserDetailsRepository userRepository, UserDetailsMapper userMapper) {
		super();
		this.userRepository = userRepository;
		this.userMapper = userMapper;
	}

	@Override
	public UserDetailsDTO findDtoByUsername(String username) {
		return userMapper.customUserDetailsToUserDetailsDTO(userRepository.findByUsername(username));
	}

	@Override
	public List<UserDetailsDTO> findAll() {
		return userRepository.findAll().stream()
				.map(userMapper::customUserDetailsToUserDetailsDTO)
				.collect(Collectors.toList());
	}

	@Override
	public CustomUserDetails findByUsername(String username) {
		
		return userRepository.findByUsername(username);
	}

	@Override
	public boolean isUsernameAvailable(String username) {
		return username != userRepository.findByUsername(username).getUsername();
	}



}
