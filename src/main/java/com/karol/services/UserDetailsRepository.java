package com.karol.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karol.domain.CustomUserDetails;

public interface UserDetailsRepository extends JpaRepository<CustomUserDetails, Long>{
	public CustomUserDetails findByUsername(String username);
}
