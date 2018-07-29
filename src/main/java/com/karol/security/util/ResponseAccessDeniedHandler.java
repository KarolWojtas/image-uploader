package com.karol.security.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class ResponseAccessDeniedHandler implements AccessDeniedHandler{

	@Override
	public void handle(HttpServletRequest arg0, HttpServletResponse response, AccessDeniedException e)
			throws IOException, ServletException {
		response.setContentType("application/json");
		response.setStatus(401);
		
	}

}
