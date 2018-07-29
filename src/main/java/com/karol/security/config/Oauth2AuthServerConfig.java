package com.karol.security.config;



import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.karol.security.util.ResponseAccessDeniedHandler;
import com.karol.services.CustomUserDetailsService;

@Configuration
@EnableAuthorizationServer
public class Oauth2AuthServerConfig extends AuthorizationServerConfigurerAdapter{
	
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
					endpoints.authenticationManager(authManager)
					.tokenEnhancer(jwtEnhancer());
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("isAuthenticated()")
				.checkTokenAccess("isAuthenticated()")
				//To pozwoliło korzystać z bcrypt!!!
				.passwordEncoder(passwordEncoder)
				;
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient("angularClient")
				.autoApprove(true)
				.authorizedGrantTypes("password","authorization_code", "refresh_token")
				.accessTokenValiditySeconds(120)
				.refreshTokenValiditySeconds(120)
				.scopes("read");
	}
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(jwtEnhancer());
	}
	@Bean 
	public JwtAccessTokenConverter jwtEnhancer() {
		KeyStoreKeyFactory keyfactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "secretKey".toCharArray());
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setKeyPair(keyfactory.getKeyPair("jwt"));
		return converter;
	}
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new ResponseAccessDeniedHandler();
	}
	
	
}
