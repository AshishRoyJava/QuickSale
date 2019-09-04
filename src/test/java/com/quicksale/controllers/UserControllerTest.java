package com.quicksale.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.quicksale.dtos.MessageDTO;
import com.quicksale.services.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
	
	private MockMvc mockMvc;
	
	@Mock
	private UserService userServiceMock;
	
	@InjectMocks
	private UserController userControllerUnderTest;

	@Before
	public void setUp() {
		
		Mockito.reset(userServiceMock);

		mockMvc = MockMvcBuilders.standaloneSetup(userControllerUnderTest).build();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void sendRegistrationInvites_ShouldReturnSuccessMessage() throws Exception {
		
		MessageDTO messageDTO = new MessageDTO("Invites will be sent to all users");
		
		when(userServiceMock.sendRegistrationInvites()).thenReturn(messageDTO);
		
		mockMvc.perform(get("/users/send-registration-invite"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.message", is("Invites will be sent to all users")));
		
		verify(userServiceMock, times(1)).sendRegistrationInvites();
		verifyNoMoreInteractions(userServiceMock);
	}

}
