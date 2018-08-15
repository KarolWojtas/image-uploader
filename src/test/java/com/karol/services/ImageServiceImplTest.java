package com.karol.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.mock.web.MockMultipartFile;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Stream;

import com.karol.domain.CustomUserDetails;
import com.karol.domain.ImageHolder;
import com.karol.domain.ImageHolderDTO;
import com.karol.domain.PageDto;
import com.karol.domain.mappers.ImageHolderMapper;
import com.karol.exceptions.BadFormatException;

class ImageServiceImplTest {
	@Mock
	private ImageHolderRepository imageRepository;
	private ImageHolderMapper mapper = ImageHolderMapper.INSTANCE;
	private LinkService linkService = new LinkService();
	private ImageService imageService;
	CustomUserDetails user;
	ImageHolder imageHolder1;
	ImageHolder imageHolder2;
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		imageService = new ImageServiceImpl(imageRepository, mapper, linkService);
		user = CustomUserDetails.builder().id(1l).username("username1").build();
		imageHolder1 = ImageHolder.builder().id(1l).description("description").imageFormat("png")
					.isPublic(true).user(user).image("this is an image".getBytes()).build();
		imageHolder2 = ImageHolder.builder().id(2l).description("desc2").imageFormat("jpg")
					.isPublic(true).user(user).image("this is an image 2".getBytes()).build();
	}

	@Test
	void testGetImage() {
		given(imageRepository.findById(anyLong())).willReturn(Optional.of(this.imageHolder1));
		
		ImageHolder returnedImageHolder = imageService.getImage(2l);
		
		assertNotNull(returnedImageHolder);
		verify(imageRepository, times(1)).findById(anyLong());
	
	}
	@TestFactory
	@DisplayName("test for saveImage with three valid extensions")
	Stream<DynamicTest> testSaveImage(){
		return Arrays.asList(new MockMultipartFile("file1", "image.png", "image/png", "this is png image".getBytes()),
				new MockMultipartFile("file2", "image.jpeg", "image/jpeg", "this is jpeg image".getBytes()),
				new MockMultipartFile("file3", "image.jpg", "image/jpg", "this is jpg image".getBytes())
				).stream().map(mockFile -> DynamicTest.dynamicTest("test for file: "+mockFile.getOriginalFilename(), ()->{
					ArgumentCaptor<ImageHolder> imageholderCaptor = ArgumentCaptor.forClass(ImageHolder.class);
					given(imageRepository.save(imageholderCaptor.capture())).willReturn(imageHolder1);
					
					boolean saveSuccessful =false;
					try {
						 saveSuccessful = this.imageService.saveImage(mockFile, this.user, "mock image desc", true);
					} catch (IOException | BadFormatException e) {
						
					} 
					assertTrue(saveSuccessful);
					assertAll(
							()->assertEquals(mockFile.getBytes(), imageholderCaptor.getValue().getImage()),
							()->assertEquals(mockFile.getOriginalFilename().split("\\.")[1], imageholderCaptor.getValue().getImageFormat())
							);
				}));
	}
	@Test
	@DisplayName("imageService should throw BadFormatException")
	void testSaveImageFormatInvalid(){
		assertThrows(BadFormatException.class, ()->{
			MockMultipartFile file = new MockMultipartFile("file", "image.svg", "image/svg+xml", "this is svg image".getBytes());
			imageService.saveImage(file, this.user, "should i throw an error?", false);
		});
	}
	@Test
	@DisplayName("test generation of 'self' link an conversion to dto" )
	void testGetImageDto() {
		given(imageRepository.findById(anyLong())).willReturn(Optional.of(this.imageHolder1));
		
		ImageHolderDTO imageDto = this.imageService.getImageDto(1l);
		
		assertNotNull(imageDto);
		assertAll(
				()-> assertEquals("/images/1", imageDto.getLinks().iterator().next().getHref()),
				()->assertEquals("image", imageDto.getLinks().iterator().next().getRel()),
				()->assertEquals(this.imageHolder1.getId(), imageDto.getId())
				);
	}

	@Test
	@DisplayName("test is repo method is called on delete invocation")
	void testDeleteImage() {
		imageService.deleteImage(1l);
		verify(imageRepository, times(1)).deleteById(anyLong());
	}

	@Test
	@DisplayName("test pagedto with public images")
	void testGetAllImagesByUser() {
		Page<ImageHolder> page = new PageImpl<ImageHolder>(Arrays.asList(imageHolder1,imageHolder2));
		given(imageRepository.findAllByUserOrderByTimestampDesc(any(CustomUserDetails.class), any(Pageable.class))).willReturn(page);
		
		PageDto<ImageHolderDTO> pageDto = imageService.getAllImagesByUser(user, PageRequest.of(0, 2), TimeZone.getDefault());
		
		assertNotNull(pageDto);
		assertAll(
				()-> assertEquals(page.getContent().size(), pageDto.getContent().size()),
				()->assertEquals(page.isFirst(), pageDto.isFirst())
				);
	}

	@Test
	@DisplayName("test pagedto with all images of given user")
	void testGetPaginatedPublicImages() {
		Page<ImageHolder> page = new PageImpl<ImageHolder>(Arrays.asList(imageHolder1,imageHolder2));
		given(imageRepository.findByIsPublicOrderByTimestampDesc(anyBoolean(), any(Pageable.class))).willReturn(page);
		
		PageDto<ImageHolderDTO> pageDto = imageService.getPaginatedPublicImages(PageRequest.of(0, 2), TimeZone.getDefault());
		
		assertNotNull(pageDto);
		assertAll(
				()-> assertEquals(page.getContent().size(), pageDto.getContent().size()),
				()->assertEquals(page.isFirst(), pageDto.isFirst())
				);
	}

}
