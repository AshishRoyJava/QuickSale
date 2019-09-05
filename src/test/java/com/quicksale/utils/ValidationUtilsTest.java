package com.quicksale.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.quicksale.exceptions.APIException;
import com.quicksale.models.Product;
import com.quicksale.models.RegisteredUser;
import com.quicksale.models.User;
import com.quicksale.repositories.ProductRepository;
import com.quicksale.repositories.RegisteredUserRepository;
import com.quicksale.repositories.UserRepository;

public class ValidationUtilsTest {

	private UserRepository userRepositoryMock;
	private ProductRepository productRepositoryMock;
	private RegisteredUserRepository registeredUserRepositoryMock;
	private ValidationUtils validationUtilsMock;

	@Before
	public void setUp() {

		userRepositoryMock = mock(UserRepository.class);
		productRepositoryMock = mock(ProductRepository.class);
		registeredUserRepositoryMock = mock(RegisteredUserRepository.class);

		validationUtilsMock = new ValidationUtils(userRepositoryMock, productRepositoryMock,
				registeredUserRepositoryMock);
	}

	@Test
	public void validateUser_ShouldReturnUser() {

		User user = new User("ABC", "Hyderabad", "abc@gmail.com");
		user.setId(1);

		when(userRepositoryMock.findById(any(Integer.class))).thenReturn(Optional.of(user));

		User userResult = validationUtilsMock.validateUser(1);

		verify(userRepositoryMock, times(1)).findById(any(Integer.class));
		verifyNoMoreInteractions(userRepositoryMock);

		assertThat(userResult.getName(), is(user.getName()));
		assertThat(userResult.getAddress(), is(user.getAddress()));
		assertThat(userResult.getEmail(), is(user.getEmail()));
	}

	@Test(expected = APIException.class)
	public void validateUser_InvalidId_ShouldThrowAPIException() throws APIException {

		when(userRepositoryMock.findById(any(Integer.class))).thenReturn(Optional.empty());

		validationUtilsMock.validateUser(1);

		verify(userRepositoryMock, times(1)).findById(any(Integer.class));
		verifyNoMoreInteractions(userRepositoryMock);

	}
	
	@Test
	public void validateProduct_ShouldReturnProduct() {

		Product product = new Product("Product1", "Expensive", 5000.0);
		product.setId(1);

		when(productRepositoryMock.findById(any(Integer.class))).thenReturn(Optional.of(product));

		Product productResult = validationUtilsMock.validateProduct(1);

		verify(productRepositoryMock, times(1)).findById(any(Integer.class));
		verifyNoMoreInteractions(productRepositoryMock);

		assertThat(productResult.getName(), is(product.getName()));
		assertThat(productResult.getDetails(), is(product.getDetails()));
		assertThat(productResult.getPrice(), is(product.getPrice()));
	}

	@Test(expected = APIException.class)
	public void validateProduct_InvalidId_ShouldThrowAPIException() throws APIException {

		when(productRepositoryMock.findById(any(Integer.class))).thenReturn(Optional.empty());

		validationUtilsMock.validateProduct(1);

		verify(productRepositoryMock, times(1)).findById(any(Integer.class));
		verifyNoMoreInteractions(productRepositoryMock);

	}
	
	@Test
	public void hasUserAlreadyPurchasedProduct_ShouldReturnTrue() {
		
		User user = new User("ABC", "Hyderabad", "abc@gmail.com");
		user.setId(1);
		
		Product product = new Product("Product1", "Expensive", 5000.0);
		product.setId(1);
		
		RegisteredUser userPurchase = new RegisteredUser(user, product, 1);
		
		when(registeredUserRepositoryMock.findByUserAndProduct(any(User.class), any(Product.class))).thenReturn(userPurchase);
		
		boolean status = validationUtilsMock.hasUserAlreadyPurchasedProduct(user, product);
		
		verify(registeredUserRepositoryMock, times(1)).findByUserAndProduct(any(User.class), any(Product.class));
		verifyNoMoreInteractions(registeredUserRepositoryMock);
		
		assertThat(status, is(true));
	}
	
	@Test
	public void hasUserAlreadyPurchasedProduct_ShouldReturnFalse() {
		
		User user = new User("ABC", "Hyderabad", "abc@gmail.com");
		user.setId(1);
		
		Product product = new Product("Product1", "Expensive", 5000.0);
		product.setId(1);
		
		RegisteredUser userPurchase = new RegisteredUser(user, product, 0);
		
		when(registeredUserRepositoryMock.findByUserAndProduct(any(User.class), any(Product.class))).thenReturn(userPurchase);
		
		boolean status = validationUtilsMock.hasUserAlreadyPurchasedProduct(user, product);
		
		verify(registeredUserRepositoryMock, times(1)).findByUserAndProduct(any(User.class), any(Product.class));
		verifyNoMoreInteractions(registeredUserRepositoryMock);
		
		assertThat(status, is(false));
	}

}
