package com.quicksale.dtos;

import java.util.Date;

/**
 * DTO class for purchase details
 * @author ashishr
 *
 */
public class PurchaseDTO {

	private String userName;
	private String userAddress;
	private String userEmail;
	private String productName;
	private String productDescription;
	private double productPrice;
	private Date timeofPurchase;
	
	public PurchaseDTO() {
	}

	public PurchaseDTO(String userName, String userAddress, String userEmail, String productName,
			String productDescription, double productPrice, Date timeofPurchase) {
		this.userName = userName;
		this.userAddress = userAddress;
		this.userEmail = userEmail;
		this.productName = productName;
		this.productDescription = productDescription;
		this.productPrice = productPrice;
		this.timeofPurchase = timeofPurchase;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public Date getTimeofPurchase() {
		return timeofPurchase;
	}

	public void setTimeofPurchase(Date timeofPurchase) {
		this.timeofPurchase = timeofPurchase;
	}

}
