package com.quicksale.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quicksale.dtos.MessageDTO;
import com.quicksale.dtos.UserRegistrationAndPurchaseDTO;
import com.quicksale.services.RegisteredUserService;

/**
 * Controller class for user registration for flas sale
 * 
 * @author ashishr
 *
 */
@RestController
@RequestMapping("/registration")
public class RegistrationController {

	private RegisteredUserService registeredUserService;

	/**
	 * Constructor based dependency injection
	 * 
	 * @param registeredUserService
	 */
	public RegistrationController(RegisteredUserService registeredUserService) {
		this.registeredUserService = registeredUserService;
	}

	/**
	 * Controller method for user registration for flash sale
	 * 
	 * @param userRegistrationDTO The user and product details
	 * @return {@link MessageDTO}
	 */
	@PostMapping("/for-sale")
	public MessageDTO registerForSale(@RequestBody UserRegistrationAndPurchaseDTO userRegistrationDTO) {
		return registeredUserService.registerForSale(userRegistrationDTO);
	}

}
