package com.karol.security.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.FileCopyUtils;
@Configuration
@EnableResourceServer
public class Oauth2resourceServerConfig extends ResourceServerConfigurerAdapter{
	@Autowired
	private AccessDeniedHandler accessDeniedHandler;
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/test").authenticated()
				.antMatchers("/test/**").permitAll()
				.antMatchers("/images/**").permitAll()
				.anyRequest() .authenticated()
			.and()
			.exceptionHandling()
				.accessDeniedHandler(accessDeniedHandler);
	}
	@Bean
	public JwtAccessTokenConverter jwtTokenEnhancer() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		Resource resource = new ClassPathResource("public.cert");
		String publicKey="";
		try {
			publicKey = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		converter.setVerifierKey(publicKey);
		return converter;
	}
	@Bean
	public TokenStore jwtTokenStore() {
		return new JwtTokenStore(jwtTokenEnhancer());
	}
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(jwtTokenStore());
	}
	
	
	
}
