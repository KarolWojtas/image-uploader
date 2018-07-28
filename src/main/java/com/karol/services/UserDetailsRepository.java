package com.karol.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karol.security.util.CustomUserDetails;

public interface UserDetailsRepository extends JpaRepository<CustomUserDetails, Long>{
	public CustomUserDetails findByUsername(String username);
}
