package com.quicksale.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * Model class to represent registered user table
 * 
 * @author ashishr
 *
 */
@Entity
public class RegisteredUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne
	@JoinColumn(name = "userId", referencedColumnName = "id")
	private User user;

	@OneToOne
	@JoinColumn(name = "productId", referencedColumnName = "id")
	private Product product;

	private int purchaseCount;

	public RegisteredUser() {
	}

	public RegisteredUser(User user, Product product, int purchaseCount) {
		this.user = user;
		this.product = product;
		this.purchaseCount = purchaseCount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getPurchaseCount() {
		return purchaseCount;
	}

	public void setPurchaseCount(int purchaseCount) {
		this.purchaseCount = purchaseCount;
	}

}
