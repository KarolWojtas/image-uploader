package com.karol.domain.mappers;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.karol.domain.ImageHolder;
import com.karol.domain.ImageHolderDTO;
import com.karol.domain.PageDto;
import com.karol.services.LinkService;

@Component
public class ImagePageMapper {
	private ImageHolderMapper mapper;
	private LinkService linkService;

	@Autowired
	public ImagePageMapper(ImageHolderMapper mapper, LinkService linkService) {
		super();
		this.mapper = mapper;
		this.linkService = linkService;
	}

	public PageDto<ImageHolderDTO> pageToPageDto(Page<ImageHolder> page, boolean isPublic) {
		PageDto<ImageHolderDTO> pageDto = new PageDto<>();
		if (page != null) {
			pageDto.setContent(
					page.getContent().stream().map(mapper::imageHolderToImageHolderDto)
						.map(this::addSelfLink)
						.collect(Collectors.toList()));
			pageDto.setFirst(page.isFirst());
			pageDto.setLast(page.isLast());
			pageDto.setNumber(page.getNumber());
			pageDto.setNumberOfELements(page.getNumberOfElements());
			pageDto.setPageable(page.getPageable());
			pageDto.setSize(page.getSize());
			pageDto.setSort(page.getSort());
			pageDto.setTotalElements(page.getTotalElements());
			pageDto.setTotalPages(page.getTotalPages());
			if(isPublic) {
				pageDto.setLinks(linkService.getPublicImagePageLinks(page));
			} else {
				pageDto.setLinks(linkService.getPrivateImagePageLinks(page));
			}
			
			return pageDto;
		}
		return new PageDto<ImageHolderDTO>();
	}
	private ImageHolderDTO addSelfLink(ImageHolderDTO dto) {
		dto.addLink(linkService.getImagelink(dto));
		return dto;
	}
}
