package com.karol.domain.mappers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.GrantedAuthority;

import com.karol.domain.CustomUserDetails;
import com.karol.domain.UserDetailsDTO;

@Mapper(componentModel="spring")
public interface UserDetailsMapper {
	UserDetailsMapper INSTANCE = Mappers.getMapper(UserDetailsMapper.class);
	@Mapping(source="authorities",target="authorities" ,ignore=true)
	CustomUserDetails userDetailsDTOToCustomUserDetails(UserDetailsDTO userDetailsDto);
	UserDetailsDTO customUserDetailsToUserDetailsDTO(CustomUserDetails customUserDetails);
	//@Mapping(source="authorities", target="authorities")
	default List<? extends GrantedAuthority> authoritiesToAuthorities(Collection<? extends GrantedAuthority> collection){
		return collection.stream().collect(Collectors.toList());
	}
}
