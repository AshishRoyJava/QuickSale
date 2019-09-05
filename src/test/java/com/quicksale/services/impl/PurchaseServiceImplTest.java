package com.quicksale.services.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.env.Environment;

import com.quicksale.dtos.MessageDTO;
import com.quicksale.dtos.PurchaseDTO;
import com.quicksale.dtos.UserRegistrationAndPurchaseDTO;
import com.quicksale.exceptions.APIException;
import com.quicksale.models.Product;
import com.quicksale.models.Purchase;
import com.quicksale.models.RegisteredUser;
import com.quicksale.models.User;
import com.quicksale.repositories.PurchaseRepository;
import com.quicksale.utils.PurchaseUtils;
import com.quicksale.utils.ValidationUtils;

public class PurchaseServiceImplTest {

	private Environment environmentMock;

	private ValidationUtils validationUtilsMock;

	private ServletContext servletContextMock;

	private PurchaseUtils purchaseUtilsMock;

	private PurchaseRepository purchaseRepositoryMock;

	private PurchaseServiceImpl purchaseServiceImplMock;

	@Before
	public void setUp() {

		environmentMock = mock(Environment.class);
		validationUtilsMock = mock(ValidationUtils.class);
		servletContextMock = mock(ServletContext.class);
		purchaseUtilsMock = mock(PurchaseUtils.class);
		purchaseRepositoryMock = mock(PurchaseRepository.class);

		purchaseServiceImplMock = new PurchaseServiceImpl(environmentMock, validationUtilsMock, servletContextMock,
				purchaseUtilsMock, purchaseRepositoryMock);
	}

	@Test
	public void getAllPurchaseOrders_ShouldReturnListOfOrders() {

		User user1 = new User("ABC", "Hyderabad", "abc@gmail.com");
		user1.setId(1);

		Product product1 = new Product("Product1", "Expensive", 5000.0);
		product1.setId(1);

		User user2 = new User("DEF", "Hyderabad", "def@gmail.com");
		user2.setId(2);

		Product product2 = new Product("Product2", "Cheap", 2000.0);
		product2.setId(2);

		Purchase first = new Purchase(product1, user1, new Date(System.currentTimeMillis()));
		Purchase second = new Purchase(product2, user2, new Date(System.currentTimeMillis()));

		when(purchaseRepositoryMock.findAll()).thenReturn(Arrays.asList(first, second));
		
		PurchaseDTO firsDTO = new PurchaseDTO("ABC", "Hyderabad", "abc@gmail.com", "Product1", "Expensive", 5000.0,
				new Date(System.currentTimeMillis()));
		PurchaseDTO secondDTO = new PurchaseDTO("DEF", "Hyderabad", "def@gmail.com", "Product2", "Cheap", 2000.0,
				new Date(System.currentTimeMillis()));
		
		when(purchaseUtilsMock.mapPurchaseToDTO(anyList())).thenReturn(Arrays.asList(firsDTO, secondDTO));
		
		List<PurchaseDTO> allPurchases = purchaseServiceImplMock.getAllPurchaseOrders();
		
		verify(purchaseRepositoryMock, times(1)).findAll();
		verifyNoMoreInteractions(purchaseRepositoryMock);
		verify(purchaseUtilsMock, times(1)).mapPurchaseToDTO(Arrays.asList(first, second));
		verifyNoMoreInteractions(purchaseUtilsMock);
		
		assertThat(allPurchases.size(), is(2));
		assertThat(allPurchases.get(0).getUserName(), is(user1.getName()));
		assertThat(allPurchases.get(0).getUserEmail(), is(user1.getEmail()));
		assertThat(allPurchases.get(0).getUserAddress(), is(user1.getAddress()));
		assertThat(allPurchases.get(0).getProductName(), is(product1.getName()));
		assertThat(allPurchases.get(0).getProductDescription(), is(product1.getDetails()));
		assertThat(allPurchases.get(0).getProductPrice(), is(product1.getPrice()));
		
		assertThat(allPurchases.get(1).getUserName(), is(user2.getName()));
		assertThat(allPurchases.get(1).getUserEmail(), is(user2.getEmail()));
		assertThat(allPurchases.get(1).getUserAddress(), is(user2.getAddress()));
		assertThat(allPurchases.get(1).getProductName(), is(product2.getName()));
		assertThat(allPurchases.get(1).getProductDescription(), is(product2.getDetails()));
		assertThat(allPurchases.get(1).getProductPrice(), is(product2.getPrice()));
		
	}

	@Test(expected = APIException.class)
	public void purchaseProduct_DateParsingError_ShouldThrowAPIException() throws APIException {
		
		UserRegistrationAndPurchaseDTO userRegistrationDTO = new UserRegistrationAndPurchaseDTO(1, 1);
		when(environmentMock.getProperty(any(String.class))).thenReturn("2019/09/10 abcd 12:00:00");
		purchaseServiceImplMock.purchaseProduct(userRegistrationDTO);
		verifyZeroInteractions(purchaseRepositoryMock);
		verify(environmentMock,times(1)).getProperty("user.purchase.time.start");
		verifyNoMoreInteractions(environmentMock);
	}
	
	@Test(expected = APIException.class)
	public void purchaseProduct_SaleNotStarted_ShouldThrowAPIException() throws APIException {

		UserRegistrationAndPurchaseDTO userRegistrationDTO = new UserRegistrationAndPurchaseDTO(1, 1);

		when(environmentMock.getProperty(any(String.class))).thenReturn("2019/09/11 12:00:00");

		purchaseServiceImplMock.purchaseProduct(userRegistrationDTO);

		verify(environmentMock, times(1)).getProperty("user.purchase.time.start");
		verifyNoMoreInteractions(environmentMock);

	}
	
	@Test(expected = APIException.class)
	public void purchaseProduct_UserAlreadyPurchased_ShouldThrowAPIException() throws APIException {

		UserRegistrationAndPurchaseDTO userRegistrationDTO = new UserRegistrationAndPurchaseDTO(1, 1);
		User user = new User("ABC", "hyderabad", "abc@gmail.com");
		user.setId(userRegistrationDTO.getUserId());

		Product product = new Product("Product1", "Expensive", 5000.0);
		product.setId(1);

		RegisteredUser registeredUser = new RegisteredUser(user, product, 1);
		registeredUser.setId(1);

		when(environmentMock.getProperty(any(String.class))).thenReturn("2019/09/5 12:00:00");
		when(validationUtilsMock.validateUser(any(Integer.class))).thenReturn(user);
		when(validationUtilsMock.validateProduct(any(Integer.class))).thenReturn(product);
		when(validationUtilsMock.hasUserAlreadyPurchasedProduct(any(User.class), any(Product.class)))
				.thenReturn(true);

		purchaseServiceImplMock.purchaseProduct(userRegistrationDTO);
		verify(environmentMock,times(1)).getProperty("user.purchase.time.start");
		verifyNoMoreInteractions(environmentMock);
		verify(validationUtilsMock, times(1)).validateUser(user.getId());
		verify(validationUtilsMock, times(1)).validateProduct(product.getId());
		verify(validationUtilsMock, times(1)).hasUserAlreadyPurchasedProduct(user, product);
		verifyNoMoreInteractions(validationUtilsMock);

	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = APIException.class)
	public void purchaseProduct_ProductOutOfStock_ShouldThrowAPIException() throws APIException {

		UserRegistrationAndPurchaseDTO purchaseDTO = new UserRegistrationAndPurchaseDTO(1, 1);
		User user = new User("ABC", "hyderabad", "abc@gmail.com");
		user.setId(purchaseDTO.getUserId());

		Product product = new Product("Product1", "Expensive", 5000.0);
		product.setId(1);

		RegisteredUser registeredUser = new RegisteredUser(user, product, 1);
		registeredUser.setId(1);
		
		Map<Integer, Integer> stockMapMock = new HashMap<>();
		stockMapMock.put(1, 0);
		stockMapMock.put(2, 0);

		when(environmentMock.getProperty(any(String.class))).thenReturn("2019/09/5 12:00:00");
		when(validationUtilsMock.validateUser(any(Integer.class))).thenReturn(user);
		when(validationUtilsMock.validateProduct(any(Integer.class))).thenReturn(product);
		when(validationUtilsMock.hasUserAlreadyPurchasedProduct(any(User.class), any(Product.class)))
				.thenReturn(false);
		when(((Map<Integer, Integer>)servletContextMock.getAttribute(any(String.class)))).thenReturn(stockMapMock);

		purchaseServiceImplMock.purchaseProduct(purchaseDTO);
		verify(environmentMock,times(1)).getProperty("user.purchase.time.start");
		verifyNoMoreInteractions(environmentMock);
		verify(validationUtilsMock, times(1)).validateUser(user.getId());
		verify(validationUtilsMock, times(1)).validateProduct(product.getId());
		verify(validationUtilsMock, times(1)).hasUserAlreadyPurchasedProduct(user, product);
		verify(servletContextMock, times(1)).getAttribute("stock");
		verifyNoMoreInteractions(validationUtilsMock);
		verifyNoMoreInteractions(servletContextMock);

	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void purchaseProduct_OrderSuccess_ShouldReturnSuccessMessage() {

		UserRegistrationAndPurchaseDTO purchaseDTO = new UserRegistrationAndPurchaseDTO(1, 1);
		User user = new User("ABC", "hyderabad", "abc@gmail.com");
		user.setId(purchaseDTO.getUserId());

		Product product = new Product("Product1", "Expensive", 5000.0);
		product.setId(1);

		RegisteredUser registeredUser = new RegisteredUser(user, product, 1);
		registeredUser.setId(1);
		
		Map<Integer, Integer> stockMapMock = new HashMap<>();
		stockMapMock.put(1, 1);
		stockMapMock.put(2, 1);

		when(environmentMock.getProperty(any(String.class))).thenReturn("2019/09/5 12:00:00");
		when(validationUtilsMock.validateUser(any(Integer.class))).thenReturn(user);
		when(validationUtilsMock.validateProduct(any(Integer.class))).thenReturn(product);
		when(validationUtilsMock.hasUserAlreadyPurchasedProduct(any(User.class), any(Product.class)))
				.thenReturn(false);
		when(((Map<Integer, Integer>)servletContextMock.getAttribute(any(String.class)))).thenReturn(stockMapMock);
		doNothing().when(purchaseUtilsMock).createPurchaseOrder(user, product);

		MessageDTO successMessage = purchaseServiceImplMock.purchaseProduct(purchaseDTO);
		verify(environmentMock,times(1)).getProperty("user.purchase.time.start");
		verifyNoMoreInteractions(environmentMock);
		verify(validationUtilsMock, times(1)).validateUser(user.getId());
		verify(validationUtilsMock, times(1)).validateProduct(product.getId());
		verify(validationUtilsMock, times(1)).hasUserAlreadyPurchasedProduct(user, product);
		verify(servletContextMock, times(1)).getAttribute("stock");
		verify(servletContextMock, times(1)).setAttribute("stock", stockMapMock);
		verifyNoMoreInteractions(validationUtilsMock);
		verifyNoMoreInteractions(servletContextMock);
		verify(purchaseUtilsMock, times(1)).createPurchaseOrder(user, product);
		verifyNoMoreInteractions(purchaseUtilsMock);
		
		assertThat(successMessage.getMessage(), is("Order placed Successfully"));

	}
}
