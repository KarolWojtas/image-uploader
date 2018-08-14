package com.karol.services;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import com.karol.controllers.ImageController;
import com.karol.domain.ImageHolder;
import com.karol.domain.ImageHolderDTO;

@Service
public class LinkService {
	public  Link getImagelink(ImageHolderDTO dto) {
		return linkTo(methodOn(ImageController.class).getImage(dto.getId())).withRel("image");
	}
	public List<Link> getPublicImagePageLinks(Page<ImageHolder> page){
		List<Link> linkList = new ArrayList<Link>();
		linkList.add(this.addPublicImagePageLink(0, page.getSize(), "first"));
		if(!page.isFirst()) {
			linkList.add(this.addPublicImagePageLink(page.getNumber()-1, page.getSize(), "prev"));
		}
		if(!page.isLast()) {
			
				linkList.add(this.addPublicImagePageLink(page.getNumber()+1, page.getSize(), "next"));
			
			linkList.add(this.addPublicImagePageLink(page.getTotalPages()-1, page.getSize(), "last"));
		}
		return linkList;
	}
	private Link addPublicImagePageLink(int pageNumber, int size, String relName) {
		return linkTo(methodOn(ImageController.class)
				.pagePagedPublicImages(String.valueOf(pageNumber), String.valueOf( size), TimeZone.getDefault()))
				.withRel(relName);
	}
	private Link addPrivateImagePageLink(int pageNumber, int size, String relName) {
		return linkTo(methodOn(ImageController.class)
				.getImagesOfPrincipal(String.valueOf(pageNumber), String.valueOf( size), TimeZone.getDefault()))
				.withRel(relName);
	}
	public List<Link> getPrivateImagePageLinks(Page<ImageHolder> page){
		List<Link> linkList = new ArrayList<Link>();
		linkList.add(this.addPrivateImagePageLink(0, page.getSize(), "first"));
		if(!page.isFirst()) {
			linkList.add(this.addPrivateImagePageLink(page.getNumber()-1, page.getSize(), "prev"));
		}
		if(!page.isLast()) {
			linkList.add(this.addPrivateImagePageLink(page.getNumber()+1, page.getSize(), "next"));
			linkList.add(this.addPrivateImagePageLink(page.getTotalPages()-1, page.getSize(), "last"));
		}
		return linkList;
	}
}
