package com.quicksale.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quicksale.dtos.MessageDTO;
import com.quicksale.services.UserService;

/**
 * Controller class for user related operations
 * 
 * @author ashishr
 *
 */
@RestController
@RequestMapping("/users")
public class UserController {

	private UserService userService;

	/**
	 * Constructor based dependency injection
	 * 
	 * @param userService
	 */
	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Controller method to send flash sale registration invite to all the users
	 * 
	 * @return {@link MessageDTO}
	 */
	@GetMapping("/send-registration-invite")
	public MessageDTO sendRegistrationInvites() {
		return userService.sendRegistrationInvites();
	}

}
