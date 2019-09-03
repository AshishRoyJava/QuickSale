package com.quicksale.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String details;
	private double price;
	
	@JsonIgnore
	@OneToOne(mappedBy = "product")
	private UserPurchasedProduct userPurchasedProduct;
	
	@JsonIgnore
	@OneToOne(mappedBy = "product")
	private Inventory inventory;
	
	@JsonIgnore
	@OneToOne(mappedBy = "product")
	private Purchase purchase;

	public Product() {
	}

	public Product(String name, String details, double price) {
		this.name = name;
		this.details = details;
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public UserPurchasedProduct getUserPurchasedProduct() {
		return userPurchasedProduct;
	}

	public void setUserPurchasedProduct(UserPurchasedProduct userPurchasedProduct) {
		this.userPurchasedProduct = userPurchasedProduct;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}

}
