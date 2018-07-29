package com.karol.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.karol.domain.CustomUserDetails;
import com.karol.domain.ImageHolder;

public interface ImageService {
	ImageHolder getImage(Long imageId);
	boolean saveImage(MultipartFile image, CustomUserDetails user, String description) throws IOException;
}
