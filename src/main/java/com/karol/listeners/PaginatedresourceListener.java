package com.karol.listeners;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public class PaginatedresourceListener implements ApplicationListener<PaginatedResourceRequestEvent>{

	@Override
	public void onApplicationEvent(PaginatedResourceRequestEvent event) {
		
		System.out.println("event");
	}

}
