package com.quicksale.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Model class to represent User table
 * 
 * @author ashishr
 *
 */
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NonNull
	private String name;
	private String address;
	@NonNull
	private String email;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<RegisteredUser> registeredUser;

	@JsonIgnore
	@OneToOne(mappedBy = "product")
	private Purchase purchase;

	public User(String name, String address, String email) {
		this.name = name;
		this.address = address;
		this.email = email;
	}

	public User() {
		super();
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<RegisteredUser> getRegisteredUser() {
		return registeredUser;
	}

	public void setRegisteredUser(List<RegisteredUser> registeredUser) {
		this.registeredUser = registeredUser;
	}

	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}

}
