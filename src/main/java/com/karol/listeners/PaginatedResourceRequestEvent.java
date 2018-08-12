package com.karol.listeners;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
@Getter
public class PaginatedResourceRequestEvent extends ApplicationEvent{
	private HttpServletResponse response;
	public PaginatedResourceRequestEvent(Object source, HttpServletResponse response) {
		super(source);
		this.response = response;
	}

}
