package com.karol;

import java.io.IOException;
import java.security.Principal;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.karol.domain.CustomUserDetails;
import com.karol.services.ImageService;
import com.karol.services.UserDetailsRepository;

@RestController
public class TestController {
	@Autowired
	private ImageService imageService;
	@Autowired
	private UserDetailsRepository userRepository;
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/test")
	public String test(Principal principal) {
		String username = principal.getName();
		return "test "+username;
	}
	
	@GetMapping("/test/image/{id}")
	public ResponseEntity<byte[]> testImage(@PathVariable Long id){
		byte[] imageBytes = imageService.getImage(id).getImage();
		return ResponseEntity.ok(imageBytes);
	}
	@PostMapping("/test/image")
	
	public ResponseEntity<String> saveImage(@RequestParam("image") MultipartFile file,
						@RequestParam("description") String description){
		CustomUserDetails user = userRepository.findByUsername("karolw");
		boolean success = false;
		try {
			success = imageService.saveImage(file, user, description);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(success) {
			return ResponseEntity.accepted().build();
		} else {
			return ResponseEntity.badRequest().build();
		}
		
	}
	
}
