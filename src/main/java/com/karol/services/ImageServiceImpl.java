package com.karol.services;

import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.karol.controllers.ImageController;
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
	private LinkService linkService;
	
	@Autowired
	public ImageServiceImpl(ImageHolderRepository imageRepository, ImageHolderMapper mapper, LinkService linkService) {
		super();
		this.imageRepository = imageRepository;
		this.mapper = mapper;
		this.linkService = linkService;
	}

	@Override
	public ImageHolder getImage(Long imageId) {
		
		return imageRepository.findById(imageId).get();
	}

	@Override
	
	public boolean saveImage(MultipartFile image, CustomUserDetails user,String description, boolean isPublic) throws IOException, BadFormatException {
		
		ImageHolder imageHolder = ImageHolder.builder().description(description)
								.imageFormat(extractFormat(image))
								.image(image.getBytes())
								.user(user)
								.isPublic(isPublic)
								.timestamp(Instant.now().atZone(ZoneId.of("GMT+00:00")))
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
		ImageHolderDTO dto = mapper.imageHolderToImageHolderDto(imageRepository.findById(imageId).get());
		dto.addLink(this.linkService.getImagelink(dto));
		return dto;
	}

	@Override
	public void deleteImage(Long imageId) {
		imageRepository.deleteById(imageId);
		
	}
	private ImageHolderDTO addSelfLink(ImageHolderDTO dto) {
		dto.addLink(linkService.getImagelink(dto));
		return dto;
	}

	@Override
	@Transactional
	public Page<ImageHolder> getAllImagesByUser(CustomUserDetails user, Pageable page) {
		return imageRepository.findAllByUserOrderByTimestampDesc(user, page);
		
	}

	@Override
	public Page<ImageHolder> getPaginatedPublicImages(Pageable page) {
		return imageRepository.findByIsPublicOrderByTimestampDesc(true, page);
	}

	

}
