package com.quicksale.utils;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.quicksale.models.Product;
import com.quicksale.models.User;
import com.quicksale.repositories.UserRepository;
import com.quicksale.services.EmailService;

/**
 * Utility class to send various types of email
 * 
 * @author ashishr
 *
 */
@Component
public class EmailUtils {

	private UserRepository userRepository;

	private EmailService emailService;

	/**
	 * Constructor based dependency injection
	 * 
	 * @param userRepository
	 * @param emailService
	 */
	public EmailUtils(UserRepository userRepository, EmailService emailService) {
		this.userRepository = userRepository;
		this.emailService = emailService;
	}

	/**
	 * Utility method to send registration invite email to all users. This is
	 * an @Async method so that this runs on a different thread than the main
	 * thread.
	 */
	@Async
	public void sendRegistrationInviteToUsers() {

		// fetch all users
		List<User> allUsers = userRepository.findAll();

		// send email to all users
		for (User user : allUsers) {
			String subject = "Flash Sale on 11th September";
			String body = "Dear " + user.getName() + ",\n"
					+ "\tWe are running a flash sale for 2 products on 11th of September.\n"
					+ "The products are available from 10:00 am on 11th September on a special discounted price.\n"
					+ "The quatity of the items are limited so please be quick and on time on the day of sale.\n"
					+ "However, users are required to register prior to the sale. \n\n"
					+ "Please follow the link below to register.(Use Postman)\n\n"
					+ "http://localhost:8080/registration/for-sale\n" + "This is a POST Request. Body - \n" + "{\n"
					+ "\tuserId:<user id>;\n" + "\tproductId:<product id>\n" + "}\n\n\n" + "Thanks,\n"
					+ "Admin Team - QuickSale";
			emailService.sendEmail(user.getEmail(), subject, body);
		}
	}

	/**
	 * Utility method to send registration confirmation email to an user when he
	 * registers for the flash sale. This is an @Async method so that this runs on a
	 * different thread than the main thread.
	 */
	@Async
	public void sendRegistrationConfirmationEmail(User user, Product product) {

		String subject = "Flash Sale on 11th September - Registration Success";
		String body = "Dear " + user.getName() + ",\n"
				+ "You have successfully registered for the flash sale on 11th September for product :"
				+ product.getName() + ".\n" + "Please remember the flash sale is on 11th of September.\n"
				+ "The products are available from 10:00 am on 11th September on a special discounted price.\n"
				+ "The quatity of the items are limited so please be quick and on time on the day of sale.\n"
				+ "Please follow the link below to buy.(Use Postman)\n\n"
				+ "http://localhost:8080/purchases/new\n" + "This is a POST Request. Body - \n" + "{\n"
				+ "\tuserId:<user id>;\n" + "\tproductId:<product id>\n" + "}\n\n\n" + "Thanks,\n"
				+ "Admin Team - QuickSale";
		emailService.sendEmail(user.getEmail(), subject, body);
	}

	/**
	 * Utility method to send order success email when the purchase is successful
	 * for the user. This is an @Async method so that this runs on a different
	 * thread than the main thread.
	 */
	@Async
	public void sendPurchaseEmailSuccess(User user, Product product) {
		String subject = "Order Success";
		String body = "Dear " + user.getName() + ",\n" + "You have successfully purchased " + product.getName() + ".\n"
				+ "The cost of the product is :" + product.getPrice() + ".\n\n\n" + "Thanks,\n"
				+ "Admin Team - QuickSale";
		emailService.sendEmail(user.getEmail(), subject, body);
	}

	/**
	 * Utility method to send order failure email when the purchase is unsuccessful
	 * for the user. This is an @Async method so that this runs on a different
	 * thread than the main thread.
	 */
	@Async
	public void sendPurchaseEmailFailure(User user, Product product) {
		String subject = "Order Failure";
		String body = "Dear " + user.getName() + ",\n" + "We are sorry to infor that your purchase order of "
				+ product.getName() + " has failed due to some technical issues." + "Please try purchasing again."
				+ "Thanks,\n" + "Admin Team - QuickSale";
		emailService.sendEmail(user.getEmail(), subject, body);
	}
}
