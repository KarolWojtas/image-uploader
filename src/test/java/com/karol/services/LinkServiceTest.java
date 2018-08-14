package com.karol.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.Link;
import org.springframework.test.context.junit.jupiter.DisabledIf;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import com.karol.domain.CustomUserDetails;
import com.karol.domain.ImageHolder;
import com.karol.domain.ImageHolderDTO;
import com.karol.domain.PageDto;

class LinkServiceTest {
	ImageHolderDTO dto;
	ImageHolder imageHolder;
	Page<ImageHolder> page;
	LinkService linkService = new LinkService();
	@BeforeEach
	void setUp() throws Exception {
		dto = new ImageHolderDTO();
		dto.setDescription("description");
		dto.setId(1l);
		dto.setPublic(true);
		
		imageHolder = ImageHolder.builder().description("description").id(1l).imageFormat("jpg")
				.user(new CustomUserDetails()).isPublic(true).build();
		page = new PageImpl<ImageHolder>(Arrays.asList(imageHolder), PageRequest.of(0, 2),6);
	}

	@Test
	@DisplayName("Test generation of image self link")
	void testGetImagelink(TestReporter testReporter) {
		Link selfLink = linkService.getImagelink(dto);
		Link linkCheck = new Link("/images/1", "image");
		testReporter.publishEntry("selflink", selfLink.getHref());
		assertAll(
				()->assertEquals(linkCheck,selfLink),
				()->assertNotNull(selfLink)
				);
		
	}

	@Test
	@DisplayName("Test generation of links to public image page")
	void testGetPublicImagePageLinks(TestReporter testReporter) {
		List<Link> links = linkService.getPublicImagePageLinks(page);
		testReporter.publishEntry("public links", links.toString());
		assertAll(
				()-> assertNotNull(links),
				()->assertEquals(3, links.size()),
				()->assertEquals("/images?page=1&size=2", links.get(1).getHref()),
				()->assertEquals("/images?page=2&size=2", links.get(2).getHref()),
				()->assertEquals("last",links.get(2).getRel())
				);
	}

	@Test
	@DisplayName("Test generation of links to public image page")
	void testGetPrivateImagePageLinks(TestReporter testReporter) {
		List<Link> links = linkService.getPrivateImagePageLinks(page);
		testReporter.publishEntry("public links", links.toString());
		assertAll(
				()-> assertNotNull(links),
				()->assertEquals(3, links.size()),
				()->assertEquals("/images/my?page=1&size=2", links.get(1).getHref()),
				()->assertEquals("/images/my?page=2&size=2", links.get(2).getHref()),
				()->assertEquals("last",links.get(2).getRel())
				);
	}

}
