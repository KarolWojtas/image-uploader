package com.karol.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.karol.domain.CustomUserDetails;
import com.karol.domain.ImageHolderDTO;
import com.karol.exceptions.BadFormatException;
import com.karol.services.ImageService;
import com.karol.services.UserDetailsService;

@RestController
public class ImageController {
	private UserDetailsService userService;
	private ImageService imageService;
	@Autowired
	public ImageController(UserDetailsService userService, ImageService imageService) {
		super();
		this.userService = userService;
		this.imageService = imageService;
	}
	@GetMapping("/images/{id}")
	public ResponseEntity<byte[]> getImage(@PathVariable Long id){
		byte[] imageBytes = imageService.getImage(id).getImage();
		return ResponseEntity.ok(imageBytes);
	}
	@GetMapping("/images/{id}/info")
	public ResponseEntity<ImageHolderDTO> getImageDto(@PathVariable Long id){
		return ResponseEntity.ok(imageService.getImageDto(id));
	}
	@PostMapping("/images")
	public ResponseEntity<String> saveImage(@RequestParam("image") MultipartFile file,
						@RequestParam("description")String description) throws IOException, BadFormatException{
		CustomUserDetails user = userService.findByUsername("karolw");
			imageService.saveImage(file, user,description);
			return ResponseEntity.accepted().build();
		
		
	}
	@GetMapping(value="/images")
	public List<ImageHolderDTO> pagePublicImages(@RequestParam(name="page", defaultValue="1")String page){
		return imageService.getPublicImages();
	}
}
