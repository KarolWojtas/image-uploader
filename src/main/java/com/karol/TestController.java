package com.karol;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value="/test", produces= {"application/json"})
	public String test() {
		return "test";
	}
}
