package com.karol.domain.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.karol.domain.ImageHolder;
import com.karol.domain.ImageHolderDTO;
import com.karol.domain.PageDto;
import com.karol.services.LinkService;

class ImagePageMapperTest {
	LinkService linkService;
	ImageHolderMapper imageMapper;
	ImagePageMapper pageMapper;
	Page<ImageHolder> page;
	ImageHolder imageHolder;
	@BeforeEach
	void setUp() throws Exception {
		linkService = new LinkService();
		imageMapper = ImageHolderMapper.INSTANCE;
		pageMapper = new ImagePageMapper(imageMapper, linkService);
		imageHolder = ImageHolder.builder().id(1l).description("description").isPublic(true)
				.timestamp(Instant.now().atZone(ZoneId.systemDefault())).build();
		page = new PageImpl<ImageHolder>(Arrays.asList(imageHolder), PageRequest.of(0, 2), 4);
		
	}

	@Test
	@DisplayName("test conversion of Page<ImageHolder> to custom PageDto")
	void testPageToPageDto(TestReporter testReporter) {
		PageDto<ImageHolderDTO> pageDto = pageMapper.pageToPageDto(page, true, TimeZone.getTimeZone("GMT"));
		testReporter.publishEntry("pageDto", pageDto.getLinks().toString());
		assertNotNull(pageDto);
		assertAll(
				()->assertEquals(page.getContent().size(), pageDto.getContent().size()),
				()->assertEquals(true,pageDto.isFirst()),
				()->assertEquals("/images?page=1&size=2", pageDto.getLinks().stream()
						.filter(link->link.getRel().equals("next")).findAny().get().getHref())
				);
	}
	

}
