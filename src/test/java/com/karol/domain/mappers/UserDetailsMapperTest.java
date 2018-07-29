package com.karol.domain.mappers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import com.karol.domain.CustomUserDetails;
import com.karol.domain.UserDetailsDTO;

@RunWith(SpringRunner.class)
class UserDetailsMapperTest {
	UserDetailsMapper mapper = UserDetailsMapper.INSTANCE;;
	CustomUserDetails details = new CustomUserDetails();
	UserDetailsDTO detailsDto = new UserDetailsDTO();
	@BeforeEach
	void setUp() throws Exception {
		
		details.setUsername("user1");
		details.setPassword("password");
		details.setFirstName("Jan");
		details.setLastName("Kowalski");
		details.setRole("ROLE_USER");
		detailsDto.setUsername("userDto");
		detailsDto.setPassword("password");
		detailsDto.setFirstName("JanDto");
		detailsDto.setLastName("KowalskiDto");
		detailsDto.setRole("ROLE_USER");
	}

	@Test
	void testCustomUserDetailsToUserDetailsDTO() {
		UserDetailsDTO userDetailsDto = mapper.customUserDetailsToUserDetailsDTO(details);
		assertAll(
				()->assertEquals("Jan", userDetailsDto.getFirstName()),
				()-> assertEquals(details.getRole(), userDetailsDto.getRole())
				);
	}

	@Test
	void testUserDetailsDTOToCustomUserDetails() {
		CustomUserDetails userDetails = mapper.userDetailsDTOToCustomUserDetails(detailsDto);
		assertAll(
				()->assertEquals("JanDto", userDetails.getFirstName()),
				()-> assertEquals(detailsDto.getRole(), userDetails.getRole())
				);
	}

}
