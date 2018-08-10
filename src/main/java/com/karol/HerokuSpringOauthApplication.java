package com.karol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;

@SpringBootApplication
public class HerokuSpringOauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(HerokuSpringOauthApplication.class, args);
	}
	@Bean
	public ControllerLinkBuilderFactory controllerLinkBuilderFactory() {
		return new ControllerLinkBuilderFactory();
	}
}
