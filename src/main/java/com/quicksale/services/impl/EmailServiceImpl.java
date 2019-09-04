package com.quicksale.services.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.quicksale.services.EmailService;

/**
 * Service class for email related operations. Implements the
 * {@link EmailService} interface
 * 
 * @author ashishr
 *
 */
@Service
public class EmailServiceImpl implements EmailService {

	private JavaMailSender emailSender;

	/**
	 * Constructor based dependency injection
	 * 
	 * @param emailSender
	 */
	public EmailServiceImpl(JavaMailSender emailSender) {
		this.emailSender = emailSender;
	}

	/**
	 * Service method to send email.
	 */
	@Override
	public void sendEmail(String to, String subject, String body) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		emailSender.send(message);
	}

}
