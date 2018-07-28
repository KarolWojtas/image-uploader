package com.karol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.karol.security.util.CustomUserDetails;
import com.karol.services.UserDetailsRepository;
@Component
public class Bootstrap implements CommandLineRunner{
	private UserDetailsRepository repository;
	@Autowired
	public Bootstrap(UserDetailsRepository repository) {
		super();
		this.repository = repository;
	}	
	@Override
	public void run(String... args) throws Exception {
		CustomUserDetails user = new CustomUserDetails();
		user.setFirstName("Karol");
		user.setLastName("Wojtas");
		user.setUsername("karolw");
		user.setPassword("$2a$10$8CzkQVbmyuRoHGkPLKPGYuZV4Y9HgoTYCi5mNupWsfZRRYDsbk7wi");
		user.setRole("ROLE_USER");
		user.setEmail("k@gmail.com");
		repository.save(user);
		
	}

}
