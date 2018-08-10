package com.karol.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedClientException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;
import com.karol.domain.CustomUserDetails;
import com.karol.domain.ImageHolder;
import com.karol.domain.ImageHolderDTO;
import com.karol.domain.Views;
import com.karol.exceptions.BadFormatException;
import com.karol.services.ImageService;
import com.karol.services.UserDetailsService;

@RestController
@CrossOrigin("http://localhost:4200")
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
		ImageHolder ih = imageService.getImage(id);
		if(ih.getUser().getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName())||ih.isPublic()) {
			byte[] imageBytes = imageService.getImage(id).getImage();
			return ResponseEntity.ok(imageBytes);
		}
		throw new UnauthorizedClientException("bad credentials");
	}
	@GetMapping("/images/{id}/info")
	@PostAuthorize("returnObject.public or authentication.name==returnObject.user.username")
	public ImageHolderDTO getImageDto(@PathVariable Long id){
		return imageService.getImageDto(id);
	}
	@PostMapping("/images")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<String> saveImage(@RequestParam("file") MultipartFile file,
						@RequestParam("description")String description, @RequestParam("isPublic") boolean isPublic) throws IOException, BadFormatException{
		CustomUserDetails user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			imageService.saveImage(file, user,description, isPublic);
			return ResponseEntity.accepted().build();
		
		
	}
	@GetMapping(value="/images/all")
	@PreAuthorize("permitAll()")
	public List<ImageHolderDTO> getAllPublicImages(){
		return imageService.getPublicImages();
	}
	@GetMapping(value="/images/paged")
	@PreAuthorize("permitAll()")
	public List<ImageHolderDTO> pagePagedPublicImages(@RequestParam(name="page", defaultValue="0")String page){
		return imageService.getSlicedPublicImages(Integer.valueOf(page), 5);
	}
	@DeleteMapping("/images/{id}")
	public void deleteImage(@PathVariable Long id) {
		ImageHolder ih = imageService.getImage(id);
		if(ih.getUser().getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
			this.imageService.deleteImage(id);
		}
	}
	@GetMapping("/images/my")
	@PreAuthorize("isAuthenticated()")
	public List<ImageHolderDTO> getImagesOfPrincipal(){
		CustomUserDetails user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		return imageService.getImagesByUser(user);
	}
}
