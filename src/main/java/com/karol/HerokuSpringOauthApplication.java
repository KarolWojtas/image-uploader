package com.karol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@SpringBootApplication
@EnableWebMvc
public class HerokuSpringOauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(HerokuSpringOauthApplication.class, args);
	}
	@Bean
	public ControllerLinkBuilderFactory controllerLinkBuilderFactory() {
		return new ControllerLinkBuilderFactory();
	}
	
}
