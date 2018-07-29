package com.karol.domain.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.karol.domain.CustomUserDetails;
import com.karol.domain.UserDetailsDTO;

@Mapper(componentModel="spring")
public interface UserDetailsMapper {
	UserDetailsMapper INSTANCE = Mappers.getMapper(UserDetailsMapper.class);
	CustomUserDetails userDetailsDTOToCustomUserDetails(UserDetailsDTO userDetailsDto);
	UserDetailsDTO customUserDetailsToUserDetailsDTO(CustomUserDetails customUserDetails);
}
