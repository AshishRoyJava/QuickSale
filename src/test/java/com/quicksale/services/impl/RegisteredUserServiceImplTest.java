package com.quicksale.services.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.core.env.Environment;

import com.quicksale.dtos.UserRegistrationAndPurchaseDTO;
import com.quicksale.exceptions.APIException;
import com.quicksale.models.Product;
import com.quicksale.models.RegisteredUser;
import com.quicksale.models.User;
import com.quicksale.repositories.RegisteredUserRepository;
import com.quicksale.utils.EmailUtils;
import com.quicksale.utils.ValidationUtils;

public class RegisteredUserServiceImplTest {

	private RegisteredUserRepository registeredUserRepositoryMock;

	private Environment environmentMock;

	private ValidationUtils validationUtilsMock;

	private RegisteredUserServiceImpl registeredUserServiceMock;

	private EmailUtils emailUtilsMock;

	@Before
	public void setUp() {

		registeredUserRepositoryMock = mock(RegisteredUserRepository.class);
		environmentMock = mock(Environment.class);
		validationUtilsMock = mock(ValidationUtils.class);
		emailUtilsMock = mock(EmailUtils.class);

		registeredUserServiceMock = new RegisteredUserServiceImpl(registeredUserRepositoryMock, environmentMock,
				emailUtilsMock, validationUtilsMock);
	}

	@Test(expected = APIException.class)
	public void registerForSale_RegistrationTimeOver_ShouldThrowAPIException() throws APIException {

		UserRegistrationAndPurchaseDTO userRegistrationDTO = new UserRegistrationAndPurchaseDTO(1, 1);

		when(environmentMock.getProperty(any(String.class))).thenReturn("2019/08/01 12:00:00");

		registeredUserServiceMock.registerForSale(userRegistrationDTO);

		verify(environmentMock, times(1)).getProperty(any(String.class));
		verifyNoMoreInteractions(environmentMock);

	}

	@Test(expected = APIException.class)
	public void registerForSale_UserAlreadyRegistered_ShouldThrowAPIException() throws APIException {

		UserRegistrationAndPurchaseDTO userRegistrationDTO = new UserRegistrationAndPurchaseDTO(1, 1);
		User user = new User("ABC", "hyderabad", "abc@gmail.com");
		user.setId(userRegistrationDTO.getUserId());

		Product product = new Product("Product1", "Expensive", 5000.0);
		product.setId(1);

		RegisteredUser registeredUser = new RegisteredUser(user, product, 0);
		registeredUser.setId(1);

		when(environmentMock.getProperty(any(String.class))).thenReturn("2019/09/10 12:00:00");
		when(validationUtilsMock.validateUser(any(Integer.class))).thenReturn(user);
		when(validationUtilsMock.validateProduct(any(Integer.class))).thenReturn(product);
		when(registeredUserRepositoryMock.findByUserAndProduct(any(User.class), any(Product.class)))
				.thenReturn(registeredUser);

		registeredUserServiceMock.registerForSale(userRegistrationDTO);
		verify(registeredUserRepositoryMock, times(1)).findByUserAndProduct(any(User.class), any(Product.class));
		verifyNoMoreInteractions(registeredUserRepositoryMock);
		verify(environmentMock,times(1)).getProperty(any(String.class));
		verifyNoMoreInteractions(environmentMock);
		verify(validationUtilsMock, times(1)).validateUser(any(Integer.class));
		verify(validationUtilsMock, times(1)).validateProduct(any(Integer.class));
		verifyNoMoreInteractions(validationUtilsMock);

	}

	@Test
	public void registerForSale_newRegistration_ShouldReturnSuccess() {

		UserRegistrationAndPurchaseDTO userRegistrationDTO = new UserRegistrationAndPurchaseDTO(1, 1);
		User user = new User("ABC", "hyderabad", "abc@gmail.com");
		user.setId(userRegistrationDTO.getUserId());

		Product product = new Product("Product1", "Expensive", 5000.0);
		product.setId(1);
		
		RegisteredUser registeredUser = new RegisteredUser(user, product, 0);

		when(environmentMock.getProperty(any(String.class))).thenReturn("2019/09/10 12:00:00");
		when(validationUtilsMock.validateUser(any(Integer.class))).thenReturn(user);
		when(validationUtilsMock.validateProduct(any(Integer.class))).thenReturn(product);
		when(registeredUserRepositoryMock.findByUserAndProduct(any(User.class), any(Product.class))).thenReturn(null);
		doNothing().when(emailUtilsMock).sendRegistrationConfirmationEmail(any(User.class), any(Product.class));
		
		registeredUserServiceMock.registerForSale(userRegistrationDTO);
		
		ArgumentCaptor<RegisteredUser> registeredUserArg = ArgumentCaptor.forClass(RegisteredUser.class);
		verify(registeredUserRepositoryMock, times(1)).save(registeredUserArg.capture());
		verify(registeredUserRepositoryMock, times(1)).findByUserAndProduct(any(User.class), any(Product.class));
		verifyNoMoreInteractions(registeredUserRepositoryMock);
		verify(environmentMock,times(1)).getProperty(any(String.class));
		verifyNoMoreInteractions(environmentMock);
		verify(validationUtilsMock, times(1)).validateUser(any(Integer.class));
		verify(validationUtilsMock, times(1)).validateProduct(any(Integer.class));
		verifyNoMoreInteractions(validationUtilsMock);
		verify(emailUtilsMock,times(1)).sendRegistrationConfirmationEmail(any(User.class), any(Product.class));
		verifyNoMoreInteractions(emailUtilsMock);

		RegisteredUser model = registeredUserArg.getValue();
		
		assertThat(model.getUser(), is(registeredUser.getUser()));
		assertThat(model.getProduct(), is(registeredUser.getProduct()));
		assertThat(model.getPurchaseCount(), is(registeredUser.getPurchaseCount()));
		assertThat(model.getId(), is(0));

	}
	
	@Test(expected = APIException.class)
	public void registerForSale_registrationFailure_shouldThrowAPIException() throws APIException {
		
		UserRegistrationAndPurchaseDTO userRegistrationDTO = new UserRegistrationAndPurchaseDTO(1, 1);
		User user = new User("ABC", "hyderabad", "abc@gmail.com");
		user.setId(userRegistrationDTO.getUserId());

		Product product = new Product("Product1", "Expensive", 5000.0);
		product.setId(1);
		
		when(environmentMock.getProperty(any(String.class))).thenReturn("2019/09/10 12:00:00");
		when(validationUtilsMock.validateUser(any(Integer.class))).thenReturn(user);
		when(validationUtilsMock.validateProduct(any(Integer.class))).thenReturn(product);
		when(registeredUserRepositoryMock.findByUserAndProduct(any(User.class), any(Product.class))).thenReturn(null);
		doThrow(APIException.class).when(registeredUserRepositoryMock).save(any(RegisteredUser.class));
		
		registeredUserServiceMock.registerForSale(userRegistrationDTO); 
		
		ArgumentCaptor<RegisteredUser> registeredUserArg = ArgumentCaptor.forClass(RegisteredUser.class);
		verify(registeredUserRepositoryMock, times(1)).save(registeredUserArg.capture());
		verify(registeredUserRepositoryMock, times(1)).findByUserAndProduct(any(User.class), any(Product.class));
		verifyNoMoreInteractions(registeredUserRepositoryMock);
		verify(environmentMock,times(1)).getProperty(any(String.class));
		verifyNoMoreInteractions(environmentMock);
		verify(validationUtilsMock, times(1)).validateUser(any(Integer.class));
		verify(validationUtilsMock, times(1)).validateProduct(any(Integer.class));
		verifyNoMoreInteractions(validationUtilsMock);
		
	}
	
	@Test(expected = APIException.class)
	public void registerForSale_dateParsingError_shouldThrowAPIException() throws APIException {
		
		UserRegistrationAndPurchaseDTO userRegistrationDTO = new UserRegistrationAndPurchaseDTO(1, 1);
		when(environmentMock.getProperty(any(String.class))).thenReturn("2019/09/10 abcd 12:00:00");
		registeredUserServiceMock.registerForSale(userRegistrationDTO);
		verifyZeroInteractions(registeredUserRepositoryMock);
		verify(environmentMock,times(1)).getProperty(any(String.class));
		verifyNoMoreInteractions(environmentMock);
		
	}
}
