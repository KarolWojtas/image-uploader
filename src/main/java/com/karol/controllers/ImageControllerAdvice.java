package com.karol.controllers;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedClientException;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.karol.exceptions.BadFormatException;

@RestControllerAdvice
public class ImageControllerAdvice {
	@ExceptionHandler(UnauthorizedUserException.class)
	@ResponseStatus(value=HttpStatus.UNAUTHORIZED)
	public String unauthorizedException(Exception e) {
		return e.getMessage();
	}
	@ExceptionHandler({IOException.class, BadFormatException.class, IndexOutOfBoundsException.class, NoSuchElementException.class})
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	public String handleException(Exception e) {
		return e.getMessage();
	}
	
}
