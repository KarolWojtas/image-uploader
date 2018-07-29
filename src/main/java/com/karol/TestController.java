package com.karol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karol.services.ImageHolderRepository;

@Controller
public class TestController {
	@Autowired
	private ImageHolderRepository imageRepository;
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value="/test", produces= {"application/json"})
	public String test() {
		return "test";
	}
	
	@GetMapping("/test/image")
	public ResponseEntity<byte[]> testImage(){
		byte[] imageBytes = imageRepository.findById(1l).get().getImage();
		return ResponseEntity.ok(imageBytes);
	}
	
}
