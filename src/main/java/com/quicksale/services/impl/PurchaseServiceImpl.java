package com.quicksale.services.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.quicksale.dtos.MessageDTO;
import com.quicksale.dtos.PurchaseDTO;
import com.quicksale.dtos.UserRegistrationAndPurchaseDTO;
import com.quicksale.exceptions.APIException;
import com.quicksale.models.Product;
import com.quicksale.models.Purchase;
import com.quicksale.models.User;
import com.quicksale.repositories.PurchaseRepository;
import com.quicksale.services.PurchaseService;
import com.quicksale.utils.PurchaseUtils;
import com.quicksale.utils.ValidationUtils;

/**
 * Service class for purchase related operations. Implements the
 * {@link PurchaseService} interface
 * 
 * @author ashishr
 *
 */
@Service
public class PurchaseServiceImpl implements PurchaseService {

	private Environment environment;

	private ValidationUtils validationUtils;

	private ServletContext servletContext;

	private PurchaseUtils purchaseUtils;
	
	private PurchaseRepository purchaseRepository;

	/**
	 * Constructor based dependency injection
	 * 
	 * @param environment
	 * @param validationUtils
	 * @param servletContext
	 * @param purchaseUtils
	 */
	public PurchaseServiceImpl(Environment environment, ValidationUtils validationUtils, ServletContext servletContext,
			PurchaseUtils purchaseUtils, PurchaseRepository purchaseRepository) {
		this.environment = environment;
		this.validationUtils = validationUtils;
		this.servletContext = servletContext;
		this.purchaseUtils = purchaseUtils;
		this.purchaseRepository = purchaseRepository;
	}

	/**
	 * Service method to place purchase order.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public MessageDTO purchaseProduct(UserRegistrationAndPurchaseDTO usPurchaseDTO) {

		// validate purchase time to check if the sale has started
		String timeLimit = environment.getProperty("user.purchase.time.start");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		try {
			Date date = sdf.parse(timeLimit);
			long millis = date.getTime();
			if (System.currentTimeMillis() < millis) {
				throw new APIException("Sale has not started yet.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new MessageDTO("Failed to parse time");
		}

		// validate user and product
		User user = validationUtils.validateUser(usPurchaseDTO.getUserId());
		Product product = validationUtils.validateProduct(usPurchaseDTO.getProductId());

		// check id the use has already purchased the product
		if (validationUtils.hasUserAlreadyPurchasedProduct(user, product)) {
			return new MessageDTO("You have already purchased the product. The quantity is limited to 1 per user");
		}

		// Decrease quantity from stock. This is done in a synchronized block to avoid
		// race condition.
		synchronized (servletContext) {
			Map<Integer, Integer> stock = (Map<Integer, Integer>) servletContext.getAttribute("stock");
			int productStock = stock.get(usPurchaseDTO.getProductId());
			if (productStock > 0) {
				productStock--;
			} else {
				return new MessageDTO("Sorry, Product is out of stock");
			}
			stock.put(usPurchaseDTO.getProductId(), productStock);
			servletContext.setAttribute("stock", stock);
		}

		// execute purchase order in a different thread
		purchaseUtils.createPurchaseOrder(user, product);
		return new MessageDTO("Order placed Successfully");
	}

	/**
	 * Service method to fetch all purchase orders
	 */
	@Override
	public List<PurchaseDTO> getAllPurchaseOrders() {
		
		// fetch al purchases
		List<Purchase> allPurchases = purchaseRepository.findAll();
		
		return purchaseUtils.mapPurchaseToDTO(allPurchases);
	}

}
