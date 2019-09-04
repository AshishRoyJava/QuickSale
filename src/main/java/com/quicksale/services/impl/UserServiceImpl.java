package com.quicksale.services.impl;

import org.springframework.stereotype.Service;

import com.quicksale.dtos.MessageDTO;
import com.quicksale.services.UserService;
import com.quicksale.utils.EmailUtils;

/**
 * Service class for User related operations. Implements {@link UserService}
 * interface
 * 
 * @author ashishr
 *
 */
@Service
public class UserServiceImpl implements UserService {

	private EmailUtils emailUtils;

	/**
	 * Constructor based dependency injection
	 * @param emailUtils
	 */
	public UserServiceImpl(EmailUtils emailUtils) {
		this.emailUtils = emailUtils;
	}

	/**
	 * Service method to send registration invites to all users 
	 */
	@Override
	public MessageDTO sendRegistrationInvites() {

		// Send registration invite email
		emailUtils.sendRegistrationInviteToUsers();
		return new MessageDTO("Invites will be sent to all users");
	}

}
