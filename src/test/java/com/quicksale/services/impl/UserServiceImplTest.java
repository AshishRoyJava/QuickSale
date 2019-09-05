package com.quicksale.services.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Before;
import org.junit.Test;

import com.quicksale.dtos.MessageDTO;
import com.quicksale.utils.EmailUtils;

public class UserServiceImplTest {
	
	private UserServiceImpl userServiceImplMock;
	
	private EmailUtils emailUtilsMock;

	@Before
	public void setUp() {
		
		emailUtilsMock = mock(EmailUtils.class);
		
		userServiceImplMock = new UserServiceImpl(emailUtilsMock);
	}

	@Test
	public void sendRegistrationInvites_ShouldReturnSuccessMessage() {
		
		doNothing().when(emailUtilsMock).sendRegistrationInviteToUsers();
		
		MessageDTO response = userServiceImplMock.sendRegistrationInvites();
		
		verify(emailUtilsMock, times(1)).sendRegistrationInviteToUsers();
		verifyNoMoreInteractions(emailUtilsMock);
		
		assertThat(response.getMessage(), is("Invites will be sent to all users"));
	}

}
