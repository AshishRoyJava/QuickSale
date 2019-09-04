package com.quicksale.services;

import com.quicksale.dtos.MessageDTO;
import com.quicksale.dtos.UserRegistrationAndPurchaseDTO;

/**
 * Service interface for registered user related operations
 * 
 * @author ashishr
 *
 */
public interface RegisteredUserService {

	public MessageDTO registerForSale(UserRegistrationAndPurchaseDTO userRegistrationDTO);
}
