package com.quicksale.services;

/**
 * Service interface for email related operations
 * 
 * @author ashishr
 *
 */
public interface EmailService {

	public void sendEmail(String to, String subject, String body);
}
