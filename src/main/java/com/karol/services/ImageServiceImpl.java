package com.karol.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.karol.domain.AcceptedImageFormats;
import com.karol.domain.CustomUserDetails;
import com.karol.domain.ImageHolder;
import com.karol.domain.ImageHolderDTO;
import com.karol.domain.mappers.ImageHolderMapper;
import com.karol.exceptions.BadFormatException;
@Service
public class ImageServiceImpl implements ImageService{
	private ImageHolderRepository imageRepository;
	private ImageHolderMapper mapper;
	private static final String BASE_URL = "http://localhost:8080/images/";
	@Autowired
	public ImageServiceImpl(ImageHolderRepository imageRepository, ImageHolderMapper mapper) {
		super();
		this.imageRepository = imageRepository;
		this.mapper = mapper;
	}

	@Override
	public ImageHolder getImage(Long imageId) {
		
		return imageRepository.findById(imageId).get();
	}

	@Override
	public boolean saveImage(MultipartFile image, CustomUserDetails user,String description) throws IOException, BadFormatException {
		
		ImageHolder imageHolder = ImageHolder.builder().description(description)
								.imageFormat(extractFormat(image))
								.image(image.getBytes())
								.user(user)
								.isPublic(true)
								.timestamp(LocalDateTime.now())
								.build();
		return imageRepository.save(imageHolder) != null;
	}
	private String extractFormat(MultipartFile file) throws BadFormatException {
		
		String suffix = file.getOriginalFilename().split("\\.")[1];
		return Arrays.asList(AcceptedImageFormats.values()).stream()
				.map(obj -> (AcceptedImageFormats) obj)
				.map(AcceptedImageFormats::getValue)
				.filter(value -> value.equals(suffix))
				.findFirst().orElseThrow(()-> new BadFormatException("file has wrong format"));
	}

	@Override
	public ImageHolderDTO getImageDto(Long imageId) {
		
		return mapper.imageHolderToImageHolderDto(imageRepository.findById(imageId).get());
	}

	@Override
	public void deleteImage(Long imageId) {
		imageRepository.deleteById(imageId);
		
	}

	@Override
	@Transactional
	public List<ImageHolderDTO> getPublicImages() {
		
		return imageRepository.findAllByIsPublic(true).stream()
				.map(mapper::imageHolderToImageHolderDto)
				.collect(Collectors.toList());
	}

	

}
