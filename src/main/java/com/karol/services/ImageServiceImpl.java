package com.karol.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.karol.domain.CustomUserDetails;
import com.karol.domain.ImageHolder;
@Service
public class ImageServiceImpl implements ImageService{
	private ImageHolderRepository imageRepository;
	@Autowired
	public ImageServiceImpl(ImageHolderRepository imageRepository) {
		super();
		this.imageRepository = imageRepository;
	}

	@Override
	public ImageHolder getImage(Long imageId) {
		
		return imageRepository.findById(imageId).get();
	}

	@Override
	public boolean saveImage(MultipartFile image, CustomUserDetails user, String description) throws IOException {
		ImageHolder imageHolder = ImageHolder.builder().description(description)
								.image(image.getBytes())
								.user(user)
								.build();
		return imageRepository.save(imageHolder) != null;
	}

}
