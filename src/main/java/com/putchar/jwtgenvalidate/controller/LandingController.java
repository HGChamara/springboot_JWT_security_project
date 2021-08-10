package com.putchar.jwtgenvalidate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api"})
public class LandingController 
{
	@GetMapping({"/greet"})
	public String greetUser() 
	{
		return "Hello user";
	}

}
