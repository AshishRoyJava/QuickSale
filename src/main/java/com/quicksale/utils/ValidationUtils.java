package com.quicksale.utils;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.quicksale.exceptions.APIException;
import com.quicksale.models.Product;
import com.quicksale.models.RegisteredUser;
import com.quicksale.models.User;
import com.quicksale.repositories.ProductRepository;
import com.quicksale.repositories.RegisteredUserRepository;
import com.quicksale.repositories.UserRepository;

/**
 * Utility class for different types of validations
 * 
 * @author ashishr
 *
 */
@Component
public class ValidationUtils {

	private UserRepository userRepository;
	private ProductRepository productRepository;
	private RegisteredUserRepository registeredUserRepository;

	/**
	 * Constructor based dependency injection
	 * 
	 * @param userRepository
	 * @param productRepository
	 * @param registeredUserRepository
	 */
	public ValidationUtils(UserRepository userRepository, ProductRepository productRepository,
			RegisteredUserRepository registeredUserRepository) {
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.registeredUserRepository = registeredUserRepository;
	}

	/**
	 * Utility method to validate the user id. If no user exists with given id and
	 * exception will be thrown.
	 * 
	 * @param userId
	 * @return
	 */
	public User validateUser(int userId) {

		Optional<User> optionalUser = userRepository.findById(userId);
		if (optionalUser.isPresent()) {
			return optionalUser.get();

		} else {
			throw new APIException("User with given id not found", HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Utility method to validate the product id. If no product exists with given id
	 * and exception will be thrown.
	 * 
	 * @param productId
	 * @return
	 */
	public Product validateProduct(int productId) {

		Optional<Product> optionalProduct = productRepository.findById(productId);
		if (optionalProduct.isPresent()) {
			return optionalProduct.get();
		} else {
			throw new APIException("Product with given id not found", HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Utility method to check if the user as already purchased a particular
	 * product.
	 * 
	 * @param user
	 * @param product
	 * @return {@link Boolean}
	 */
	public boolean hasUserAlreadyPurchasedProduct(User user, Product product) {

		RegisteredUser userPurchase = registeredUserRepository.findByUserAndProduct(user, product);
		return userPurchase.getPurchaseCount() > 0;
	}
}
