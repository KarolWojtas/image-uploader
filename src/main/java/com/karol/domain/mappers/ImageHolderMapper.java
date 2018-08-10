package com.karol.domain.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.karol.domain.ImageHolder;

import com.karol.domain.ImageHolderDTO;

@Mapper(componentModel="spring")
public interface ImageHolderMapper {
	ImageHolderMapper INSTANCE = Mappers.getMapper(ImageHolderMapper.class);
	ImageHolder imageHolderDtoToImageHolder(ImageHolderDTO imageDto);
	@Mapping(source="user.username", target="username")
	ImageHolderDTO imageHolderToImageHolderDto(ImageHolder imageHolder);
}
