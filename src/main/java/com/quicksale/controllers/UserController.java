package com.quicksale.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.quicksale.services.UserService;

@RestController
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}
		
}
