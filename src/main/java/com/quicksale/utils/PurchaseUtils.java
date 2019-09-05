package com.quicksale.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.quicksale.dtos.PurchaseDTO;
import com.quicksale.exceptions.APIException;
import com.quicksale.models.Inventory;
import com.quicksale.models.Product;
import com.quicksale.models.Purchase;
import com.quicksale.models.RegisteredUser;
import com.quicksale.models.User;
import com.quicksale.repositories.InventoryRepository;
import com.quicksale.repositories.PurchaseRepository;
import com.quicksale.repositories.RegisteredUserRepository;

/**
 * Utility class for purchase related operations
 * 
 * @author ashishr
 *
 */
@Component
public class PurchaseUtils {

	private InventoryRepository inventoryRepository;

	private EmailUtils emailUtils;

	private PurchaseRepository purchaseRepository;

	private ServletContext servletContext;

	private RegisteredUserRepository registeredUserRepository;

	/**
	 * Constructor based dependency injection
	 * 
	 * @param inventoryRepository
	 * @param emailUtils
	 * @param purchaseRepository
	 * @param servletContext
	 * @param registeredUserRepository
	 */
	public PurchaseUtils(InventoryRepository inventoryRepository, EmailUtils emailUtils,
			PurchaseRepository purchaseRepository, ServletContext servletContext,
			RegisteredUserRepository registeredUserRepository) {
		this.inventoryRepository = inventoryRepository;
		this.emailUtils = emailUtils;
		this.purchaseRepository = purchaseRepository;
		this.servletContext = servletContext;
		this.registeredUserRepository = registeredUserRepository;
	}

	/**
	 * Utility method to create a purchase order for a user. This method is a
	 * transaction. i.e. Either the complete method will be executed o if any error
	 * occurs in between then nothing will be persisted. This is and @Async method.
	 * i.e. This runs on a different thread that the main thread.
	 * 
	 * @param user
	 * @param product
	 */
	@Async
	@Transactional
	public void createPurchaseOrder(User user, Product product) {
		try {
			// Update inventory
			updateInventory(product);

			// Store purchase order information
			recordPurchaseOrder(user, product);

			// Updated user purchase count for the product
			updateUserPurchaseCount(user, product);
			
			// send order success email to the user
			emailUtils.sendPurchaseEmailSuccess(user, product);
		} catch (Exception e) {
			// In case of error update the stock as the purchase was unsuccessful
			updateStockOnContext(product);
			// send order failure email to the user
			emailUtils.sendPurchaseEmailFailure(user, product);
			throw new APIException("Failed to place purchase order", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Private method to update the user purchase count
	 * 
	 * @param user
	 * @param product
	 */
	private void updateUserPurchaseCount(User user, Product product) {
		try {
			RegisteredUser registeredUser = registeredUserRepository.findByUserAndProduct(user, product);
			registeredUser.setPurchaseCount(registeredUser.getPurchaseCount() + 1);

			registeredUserRepository.save(registeredUser);
		} catch (Exception e) {
			throw new APIException("Failed to update user purchase count", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Private method to store the purchase order
	 * 
	 * @param user
	 * @param product
	 */
	private void recordPurchaseOrder(User user, Product product) {

		try {
			purchaseRepository.save(new Purchase(product, user, new Date(System.currentTimeMillis())));
		} catch (Exception e) {
			throw new APIException("Failed to save purchase record", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Private method to update the inventory.
	 * 
	 * @param product
	 */
	private void updateInventory(Product product) {

		try {
			Inventory inventory = inventoryRepository.findByProduct(product);
			inventory.setCount(inventory.getCount() - 1);
			inventoryRepository.save(inventory);
		} catch (Exception e) {
			throw new APIException("Failed to update inventory", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Private method to update the stock on context. As the order failed, the
	 * product should be available for order again.
	 * 
	 * @param product
	 */
	@SuppressWarnings("unchecked")
	private void updateStockOnContext(Product product) {
		// synchronized method to avoid race condition
		synchronized (servletContext) {
			Map<Integer, Integer> stock = (Map<Integer, Integer>) servletContext.getAttribute("stock");
			int productStock = stock.get(product.getId());

			productStock++;

			stock.put(product.getId(), productStock);
			servletContext.setAttribute("stock", stock);
		}
	}

	/**
	 * Utility method to map purchase entity to dto object
	 * @param allPurchases
	 * @return
	 */
	public List<PurchaseDTO> mapPurchaseToDTO(List<Purchase> allPurchases) {

		List<PurchaseDTO> purchases = new ArrayList<>();
		for (Purchase purchase : allPurchases) {
			
			PurchaseDTO purchaseDTO = new PurchaseDTO();
			
			purchaseDTO.setUserName(purchase.getUser().getName());
			purchaseDTO.setUserAddress(purchase.getUser().getAddress());
			purchaseDTO.setUserEmail(purchase.getUser().getEmail());
			purchaseDTO.setProductName(purchase.getProduct().getName());
			purchaseDTO.setProductDescription(purchase.getProduct().getDetails());
			purchaseDTO.setProductPrice(purchase.getProduct().getPrice());
			purchaseDTO.setTimeofPurchase(purchase.getTimeOfPurchase());

			purchases.add(purchaseDTO);
		}
		return purchases;
	}
}
