package com.tim.gotthere_server;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/map").setViewName("map");
		registry.addViewController("/").setViewName("map");
		registry.addViewController("/login").setViewName("login");
	}
}
