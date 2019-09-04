package com.quicksale.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quicksale.dtos.MessageDTO;
import com.quicksale.dtos.PurchaseDTO;
import com.quicksale.dtos.UserRegistrationAndPurchaseDTO;
import com.quicksale.services.PurchaseService;

/**
 * Controller class for purchase related operations
 * 
 * @author ashishr
 *
 */
@RestController
@RequestMapping("/purchases")
public class PurchaseController {

	private PurchaseService purchaseService;

	/**
	 * Constructor based dependency injection
	 * 
	 * @param purchaseService
	 */
	public PurchaseController(PurchaseService purchaseService) {
		this.purchaseService = purchaseService;
	}

	/**
	 * Controller method to place a purchase order.
	 * 
	 * @param usPurchaseDTO The user and product details
	 * @return {@link MessageDTO}
	 */
	@PostMapping("/new")
	public MessageDTO purchaseProduct(@RequestBody UserRegistrationAndPurchaseDTO usPurchaseDTO) {
		return purchaseService.purchaseProduct(usPurchaseDTO);
	}

	/**
	 * Controller method to fetch all purchase orders
	 * 
	 * @return {@link List} of {@link PurchaseDTO}
	 */
	@GetMapping("/all")
	public List<PurchaseDTO> getAllPurchaseOrders() {
		return purchaseService.getAllPurchaseOrders();
	}
}
