package com.karol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.karol.security.util.CustomUserDetails;
import com.karol.services.UserDetailsRepository;
@Component
public class Bootstrap implements CommandLineRunner{
	private UserDetailsRepository repository;
	private PasswordEncoder passwordEncoder;
	@Autowired
	public Bootstrap(UserDetailsRepository repository, PasswordEncoder passwordEncoder) {
		super();
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
	}
	@Value("${admin.username}")
	private String username;
	
	@Value("${admin.password}")
	private String password;
	@Override
	public void run(String... args) throws Exception {
		CustomUserDetails user = new CustomUserDetails();
		user.setFirstName("Karol");
		user.setLastName("Wojtas");
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(password));
		user.setRole("ROLE_USER");
		user.setEmail("k@gmail.com");
		repository.save(user);
		
	}

}
