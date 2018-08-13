package com.karol;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.karol.domain.CustomUserDetails;
import com.karol.domain.ImageHolder;
import com.karol.services.ImageHolderRepository;
import com.karol.services.UserDetailsRepository;
@Component
public class Bootstrap implements CommandLineRunner{
	private UserDetailsRepository repository;
	private PasswordEncoder passwordEncoder;
	private ImageHolderRepository imageRepository;
	@Autowired
	public Bootstrap(UserDetailsRepository repository, PasswordEncoder passwordEncoder,
			ImageHolderRepository imageRepository) {
		super();
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.imageRepository = imageRepository;
	}
	@Value("${admin.username}")
	private String username;
	
	@Value("${admin.password}")
	private String password;
	@Override
	@Transactional
	public void run(String... args) throws Exception {
		if(repository.count()<=0) {
			CustomUserDetails user = new CustomUserDetails();
			user.setFirstName("Karol");
			user.setLastName("Wojtas");
			user.setUsername(username);
			user.setPassword(passwordEncoder.encode(password));
			user.setRole("ROLE_ADMIN");
			user.setEmail("k@gmail.com");
			repository.save(user);
			imageRepository.save(this.saveImage(user));
			CustomUserDetails user2 = new CustomUserDetails();
			user2.setFirstName("Magdalena");
			user2.setLastName("Dobska");
			user2.setUsername("mdobska");
			user2.setPassword(passwordEncoder.encode("henryczek123"));
			user2.setRole("ROLE_USER");
			user2.setEmail("mdobska@onet.pl");
			repository.save(user2);
		}
		
		
		
		
		
	}
	private ImageHolder saveImage(CustomUserDetails user){
		ClassPathResource imageFile = new ClassPathResource("/static/zdj1.jpg");
		ImageHolder imageHolder = new ImageHolder();
		imageHolder.setUser(user);
		byte[] imageArray = null;
		try {
			imageArray = Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/main/resources/"+imageFile.getPath()));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		imageHolder.setImage(imageArray);
		ZonedDateTime time = Instant.now().atZone(ZoneId.of("GMT+00:00"));
		System.out.println(time.toString());
		imageHolder.setTimestamp(time);
		return imageHolder;
	}

}
