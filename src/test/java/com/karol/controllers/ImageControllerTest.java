package com.karol.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karol.domain.CustomUserDetails;
import com.karol.domain.ImageHolder;
import com.karol.domain.ImageHolderDTO;
import com.karol.domain.mappers.ImageHolderMapper;
import com.karol.exceptions.BadFormatException;
import com.karol.services.ImageService;
import com.karol.services.UserDetailsService;
@RunWith(SpringRunner.class)
@WebMvcTest(ImageController.class)
public class ImageControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ImageService imageService;
	@MockBean
	private UserDetailsService userService;
	@InjectMocks
	private ImageController controller;
	private CustomUserDetails user = CustomUserDetails.builder().id(1l).username("username1").password("password").role("ROLE_ADMIN,ROLE_USER").build();
	private ImageHolder imageHolder ;
	private ImageHolderDTO imageDto ;
	@Before
	public void setUp() throws Exception {
		imageHolder = ImageHolder.builder().id(1l).description("description").image("this is an image".getBytes())
				.isPublic(true).user(user).build();
		imageDto = ImageHolderDTO.builder().id(1l).description("dto_description").isPublic(true).username(user.getUsername())
				.timestamp(Instant.now().atZone(ZoneId.systemDefault()))
				.links(Arrays.asList(new Link("/image/1", "image"))).build();
		
	}

	@Test
	@WithMockUser(username="username1", password="password", roles="USER")
	public void testGetImage() throws Exception {
		given(imageService.getImage(Mockito.anyLong())).willReturn(imageHolder);
		
		mockMvc.perform(get("/images/{id}",1).accept(MediaType.IMAGE_JPEG))
			.andExpect(jsonPath("$", is(equalTo("this is an image"))))
			.andExpect(status().isOk());
	}
	
	
	@Test
	@WithMockUser(username="username1", password="password", roles="USER")
	public void testGetImageDto() throws Exception {
		given(imageService.getImageDto(Mockito.anyLong())).willReturn(imageDto);
		
		mockMvc.perform(get("/images/{id}/info", imageDto.getId()).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.timestamp", is(CoreMatchers.instanceOf(Long.class))))
				.andExpect(jsonPath("$.links[0].rel", is(equalTo("image"))));
	
	}

	@Test
	@Ignore
	@WithMockUser(username="username1", password="password", roles="USER")
	public void testSaveImage() throws Exception {
		given(imageService.saveImage(Mockito.any(MultipartFile.class), Mockito.any(CustomUserDetails.class), anyString(), anyBoolean()))
			.willReturn(false);
		
		mockMvc.perform(multipart("/image").file("file.jpg", "image".getBytes())
				.param("description", "description")
				.param("isPublic", "true"))
			.andExpect(status().isOk());
				
	}

	@Test
	@Ignore
	public void testPagePagedPublicImages() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testDeleteImage() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGetImagesOfPrincipal() {
		fail("Not yet implemented");
	}

}
