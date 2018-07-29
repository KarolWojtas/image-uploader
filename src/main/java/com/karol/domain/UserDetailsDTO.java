package com.karol.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {

	private Long autoUserId;

	private String firstName;

	private String lastName;

	private String username;

	private String password;

	private String email;

	private String role;
}
