package com.quicksale.dtos;

/**
 * DTO class for user and product informations
 * 
 * @author ashishr
 *
 */
public class UserRegistrationAndPurchaseDTO {

	private int userId;
	private int productId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

}
