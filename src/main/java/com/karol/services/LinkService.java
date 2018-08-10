package com.karol.services;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import com.karol.controllers.ImageController;
import com.karol.domain.ImageHolderDTO;

@Service
public class LinkService {
	public  Link getImagelink(ImageHolderDTO dto) {
		return linkTo(methodOn(ImageController.class).getImage(dto.getId())).withRel("image");
	}
	
}
