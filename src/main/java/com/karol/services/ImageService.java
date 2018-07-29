package com.karol.services;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

import com.karol.domain.CustomUserDetails;
import com.karol.domain.ImageHolder;
import com.karol.domain.ImageHolderDTO;
import com.karol.exceptions.BadFormatException;

public interface ImageService {
	ImageHolder getImage(Long imageId);
	boolean saveImage(MultipartFile image, CustomUserDetails user, String description) throws IOException, BadFormatException;
	ImageHolderDTO getImageDto(Long imageId);
	void deleteImage(Long imageId);
	List<ImageHolderDTO> getPublicImages();
}
