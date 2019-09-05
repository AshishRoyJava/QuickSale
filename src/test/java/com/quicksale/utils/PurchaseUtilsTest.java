package com.quicksale.utils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;

import com.quicksale.exceptions.APIException;
import com.quicksale.models.Inventory;
import com.quicksale.models.Product;
import com.quicksale.models.Purchase;
import com.quicksale.models.RegisteredUser;
import com.quicksale.models.User;
import com.quicksale.repositories.InventoryRepository;
import com.quicksale.repositories.PurchaseRepository;
import com.quicksale.repositories.RegisteredUserRepository;

public class PurchaseUtilsTest {

	private InventoryRepository inventoryRepositoryMock;

	private EmailUtils emailUtilsMock;

	private PurchaseRepository purchaseRepositoryMock;

	private ServletContext servletContextMock;

	private RegisteredUserRepository registeredUserRepositoryMock;

	private PurchaseUtils purchaseUtilsMock;

	@Before
	public void setUp() {

		inventoryRepositoryMock = mock(InventoryRepository.class);
		emailUtilsMock = mock(EmailUtils.class);
		purchaseRepositoryMock = mock(PurchaseRepository.class);
		servletContextMock = mock(ServletContext.class);
		registeredUserRepositoryMock = mock(RegisteredUserRepository.class);

		purchaseUtilsMock = new PurchaseUtils(inventoryRepositoryMock, emailUtilsMock, purchaseRepositoryMock,
				servletContextMock, registeredUserRepositoryMock);
	}

	@Test(expected = APIException.class)
	public void createPurchaseOrder_updateInventory_FetchInventoryFailure_ShouldThrowAPIException() throws APIException{
		
		User user = new User("ABC", "Hyderabad", "abc@gmail.com");
		user.setId(1);
		
		Product product = new Product("Product1", "Expensive", 5000.0);
		product.setId(1);
		
		Map<Integer, Integer> stockMapMock = new HashMap<>();
		stockMapMock.put(1, 1);
		stockMapMock.put(2, 1);
		
		doThrow(APIException.class).when(inventoryRepositoryMock).findByProduct(any(Product.class));
		when(servletContextMock.getAttribute("stock")).thenReturn(stockMapMock);
		doNothing().when(emailUtilsMock).sendPurchaseEmailFailure(any(User.class), any(Product.class));
		
		purchaseUtilsMock.createPurchaseOrder(user, product);
		
		verify(inventoryRepositoryMock, times(1)).findByProduct(any(Product.class));
		verifyNoMoreInteractions(inventoryRepositoryMock);
		verify(servletContextMock, times(1)).getAttribute(any(String.class));
		verifyNoMoreInteractions(servletContextMock);
		verifyZeroInteractions(emailUtilsMock);
		
	}
	
	@Test(expected = APIException.class)
	public void createPurchaseOrder_updateInventory_SaveInventoryFailure_ShouldThrowAPIException() throws APIException{
		
		User user = new User("ABC", "Hyderabad", "abc@gmail.com");
		user.setId(1);
		
		Product product = new Product("Product1", "Expensive", 5000.0);
		product.setId(1);
		
		Inventory inventory = new Inventory(product, 2);
		
		Map<Integer, Integer> stockMapMock = new HashMap<>();
		stockMapMock.put(1, 1);
		stockMapMock.put(2, 1);
		
		when(inventoryRepositoryMock.findByProduct(any(Product.class))).thenReturn(inventory);
		doThrow(APIException.class).when(inventoryRepositoryMock).save(any(Inventory.class));
		when(servletContextMock.getAttribute("stock")).thenReturn(stockMapMock);
		doNothing().when(emailUtilsMock).sendPurchaseEmailFailure(any(User.class), any(Product.class));
		
		purchaseUtilsMock.createPurchaseOrder(user, product);
		
		verify(inventoryRepositoryMock, times(1)).findByProduct(any(Product.class));
		verify(inventoryRepositoryMock, times(1)).save(any(Inventory.class));
		verifyNoMoreInteractions(inventoryRepositoryMock);
		verify(servletContextMock, times(1)).getAttribute(any(String.class));
		verifyNoMoreInteractions(servletContextMock);
		verifyZeroInteractions(emailUtilsMock);
		
	}
	
	@Test(expected = APIException.class)
	public void createPurchaseOrder_recordPurchaseOrderFailure_ShouldThrowAPIException() throws APIException{
		
		User user = new User("ABC", "Hyderabad", "abc@gmail.com");
		user.setId(1);
		
		Product product = new Product("Product1", "Expensive", 5000.0);
		product.setId(1);
		
		Inventory inventory = new Inventory(product, 2);
		inventory.setId(1);
		
		Map<Integer, Integer> stockMapMock = new HashMap<>();
		stockMapMock.put(1, 1);
		stockMapMock.put(2, 1);
		
		when(inventoryRepositoryMock.findByProduct(any(Product.class))).thenReturn(inventory);
		when(inventoryRepositoryMock.save(any(Inventory.class))).thenReturn(inventory);
		doThrow(APIException.class).when(purchaseRepositoryMock).save(any(Purchase.class));
		when(servletContextMock.getAttribute("stock")).thenReturn(stockMapMock);
		doNothing().when(emailUtilsMock).sendPurchaseEmailFailure(any(User.class), any(Product.class));
		
		purchaseUtilsMock.createPurchaseOrder(user, product);
		
		verify(inventoryRepositoryMock, times(1)).findByProduct(any(Product.class));
		verify(inventoryRepositoryMock, times(1)).save(any(Inventory.class));
		verifyNoMoreInteractions(inventoryRepositoryMock);
		verify(purchaseRepositoryMock, times(1)).save(any(Purchase.class));
		verifyNoMoreInteractions(purchaseUtilsMock);
		verify(servletContextMock, times(1)).getAttribute(any(String.class));
		verifyNoMoreInteractions(servletContextMock);
		verifyZeroInteractions(emailUtilsMock);
		
	}
	
	@Test(expected = APIException.class)
	public void createPurchaseOrder_updateUserPurchaseCount_FetchUserPurchaseCountFailure_ShouldThrowAPIException() throws APIException{
		
		User user = new User("ABC", "Hyderabad", "abc@gmail.com");
		user.setId(1);
		
		Product product = new Product("Product1", "Expensive", 5000.0);
		product.setId(1);
		
		Inventory inventory = new Inventory(product, 2);
		inventory.setId(1);
		
		Purchase purchase = new Purchase(product, user, new Date(System.currentTimeMillis()));
		
		Map<Integer, Integer> stockMapMock = new HashMap<>();
		stockMapMock.put(1, 1);
		stockMapMock.put(2, 1);
		
		when(inventoryRepositoryMock.findByProduct(any(Product.class))).thenReturn(inventory);
		when(inventoryRepositoryMock.save(any(Inventory.class))).thenReturn(inventory);
		when(purchaseRepositoryMock.save(any(Purchase.class))).thenReturn(purchase);
		doThrow(APIException.class).when(registeredUserRepositoryMock).findByUserAndProduct(any(User.class), any(Product.class));
		when(servletContextMock.getAttribute("stock")).thenReturn(stockMapMock);
		doNothing().when(emailUtilsMock).sendPurchaseEmailFailure(any(User.class), any(Product.class));
		
		purchaseUtilsMock.createPurchaseOrder(user, product);
		
		verify(inventoryRepositoryMock, times(1)).findByProduct(any(Product.class));
		verify(inventoryRepositoryMock, times(1)).save(any(Inventory.class));
		verifyNoMoreInteractions(inventoryRepositoryMock);
		verify(purchaseRepositoryMock, times(1)).save(any(Purchase.class));
		verifyNoMoreInteractions(purchaseRepositoryMock);
		verify(registeredUserRepositoryMock, times(1)).findByUserAndProduct(any(User.class), any(Product.class));
		verifyNoMoreInteractions(registeredUserRepositoryMock);
		verify(servletContextMock, times(1)).getAttribute(any(String.class));
		verifyNoMoreInteractions(servletContextMock);
		verifyZeroInteractions(emailUtilsMock);
		
	}
	
	@Test(expected = APIException.class)
	public void createPurchaseOrder_UpdateUserPurchaseCount_SaveUserPurchaseCountFailure_ShouldThrowAPIException() throws APIException{
		
		User user = new User("ABC", "Hyderabad", "abc@gmail.com");
		user.setId(1);
		
		Product product = new Product("Product1", "Expensive", 5000.0);
		product.setId(1);
		
		Inventory inventory = new Inventory(product, 2);
		inventory.setId(1);
		
		Purchase purchase = new Purchase(product, user, new Date(System.currentTimeMillis()));
		
		RegisteredUser registeredUser = new RegisteredUser(user, product, 0);
		registeredUser.setId(1);
		
		Map<Integer, Integer> stockMapMock = new HashMap<>();
		stockMapMock.put(1, 1);
		stockMapMock.put(2, 1);
		
		when(inventoryRepositoryMock.findByProduct(any(Product.class))).thenReturn(inventory);
		when(inventoryRepositoryMock.save(any(Inventory.class))).thenReturn(inventory);
		when(purchaseRepositoryMock.save(any(Purchase.class))).thenReturn(purchase);
		when(registeredUserRepositoryMock.findByUserAndProduct(user, product)).thenReturn(registeredUser);
		doThrow(APIException.class).when(registeredUserRepositoryMock).save(any(RegisteredUser.class));
		when(servletContextMock.getAttribute("stock")).thenReturn(stockMapMock);
		doNothing().when(emailUtilsMock).sendPurchaseEmailFailure(any(User.class), any(Product.class));
		
		purchaseUtilsMock.createPurchaseOrder(user, product);
		
		verify(inventoryRepositoryMock, times(1)).findByProduct(any(Product.class));
		verify(inventoryRepositoryMock, times(1)).save(any(Inventory.class));
		verifyNoMoreInteractions(inventoryRepositoryMock);
		verify(purchaseRepositoryMock, times(1)).save(any(Purchase.class));
		verifyNoMoreInteractions(purchaseRepositoryMock);
		verify(registeredUserRepositoryMock, times(1)).findByUserAndProduct(any(User.class), any(Product.class));
		verify(registeredUserRepositoryMock, times(1)).save(any(RegisteredUser.class));
		verifyNoMoreInteractions(registeredUserRepositoryMock);
		verify(servletContextMock, times(1)).getAttribute(any(String.class));
		verifyNoMoreInteractions(servletContextMock);
		verifyZeroInteractions(emailUtilsMock);
		
	}

}
