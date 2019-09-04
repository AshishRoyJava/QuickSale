package com.quicksale.dtos;

/**
 * DTO class for response messages
 * 
 * @author ashishr
 *
 */
public class MessageDTO {

	private String message;

	public MessageDTO(String message) {
		this.message = message;
	}

	public MessageDTO() {
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
