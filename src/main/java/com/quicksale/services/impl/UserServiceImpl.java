package com.quicksale.services.impl;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Service;

import com.quicksale.repositories.UserRepository;
import com.quicksale.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;

	private ServletContext servletContext;

	public UserServiceImpl(UserRepository userRepository, ServletContext servletContext) {
		this.userRepository = userRepository;
		this.servletContext = servletContext;
	}

}
