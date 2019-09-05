package com.quicksale.services.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.quicksale.dtos.MessageDTO;
import com.quicksale.dtos.UserRegistrationAndPurchaseDTO;
import com.quicksale.exceptions.APIException;
import com.quicksale.models.Product;
import com.quicksale.models.RegisteredUser;
import com.quicksale.models.User;
import com.quicksale.repositories.RegisteredUserRepository;
import com.quicksale.services.RegisteredUserService;
import com.quicksale.utils.EmailUtils;
import com.quicksale.utils.ValidationUtils;

/**
 * Service class for Registered user related operations
 * 
 * @author ashishr
 *
 */
@Service
public class RegisteredUserServiceImpl implements RegisteredUserService {

	private RegisteredUserRepository registeredUserRepository;

	private Environment environment;

	private EmailUtils emailUtils;

	private ValidationUtils validationUtils;

	/**
	 * Constructor based dependency injection
	 * 
	 * @param registeredUserRepository
	 * @param environment
	 * @param emailUtils
	 * @param validationUtils
	 */
	public RegisteredUserServiceImpl(RegisteredUserRepository registeredUserRepository, Environment environment,
			EmailUtils emailUtils, ValidationUtils validationUtils) {
		this.registeredUserRepository = registeredUserRepository;
		this.environment = environment;
		this.emailUtils = emailUtils;
		this.validationUtils = validationUtils;
	}

	/**
	 * Service method to register a user for the flash sale
	 * 
	 * @param userRegistrationDTO
	 */
	@Override
	public MessageDTO registerForSale(UserRegistrationAndPurchaseDTO userRegistrationDTO) {

		// validate registration time to check if the registration time is over
		String timeLimit = environment.getProperty("user.registration.time.limit");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date;
		try {
			date = sdf.parse(timeLimit);
		} catch (ParseException e) {
			throw new APIException("Failed to parse time", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		long millis = date.getTime();
		if (System.currentTimeMillis() > millis) {
			throw new APIException("Registration time limit is over.", HttpStatus.BAD_REQUEST);
		}

		// validate user and product
		User user = validationUtils.validateUser(userRegistrationDTO.getUserId());
		Product product = validationUtils.validateProduct(userRegistrationDTO.getProductId());

		// check if the user has already registered for the product
		RegisteredUser registeredUser = registeredUserRepository.findByUserAndProduct(user, product);
		if (null != registeredUser) {
			throw new APIException("You have already registered for this product", HttpStatus.CONFLICT);
		}

		// store user registration
		try {
			registeredUserRepository.save(new RegisteredUser(user, product, 0));
		} catch (Exception e) {
			throw new APIException("Failed to register user", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// Send registration confirmation email
		emailUtils.sendRegistrationConfirmationEmail(user, product);
		return new MessageDTO("Successfully registered user for sale. You will receive a confirmation email shortly");
	}

}
