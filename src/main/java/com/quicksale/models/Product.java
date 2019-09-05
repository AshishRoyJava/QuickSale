package com.quicksale.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Model class to represent Product table
 * 
 * @author ashishr
 *
 */
@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String details;
	private double price;

	@JsonIgnore
	@OneToMany(mappedBy = "product")
	private List<RegisteredUser> registeredUser;

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

	public List<RegisteredUser> getRegisteredUser() {
		return registeredUser;
	}

	public void setRegisteredUser(List<RegisteredUser> registeredUser) {
		this.registeredUser = registeredUser;
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
