package com.quicksale.services;

import java.util.List;

import com.quicksale.dtos.MessageDTO;
import com.quicksale.dtos.PurchaseDTO;
import com.quicksale.dtos.UserRegistrationAndPurchaseDTO;

/**
 * Service interface for purchase related operations
 * 
 * @author ashishr
 *
 */
public interface PurchaseService {

	public MessageDTO purchaseProduct(UserRegistrationAndPurchaseDTO usPurchaseDTO);
	
	public List<PurchaseDTO> getAllPurchaseOrders();
}
