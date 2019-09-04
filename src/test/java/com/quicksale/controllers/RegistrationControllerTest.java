package com.quicksale.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.quicksale.dtos.MessageDTO;
import com.quicksale.dtos.UserRegistrationAndPurchaseDTO;
import com.quicksale.services.RegisteredUserService;
import com.quicksale.utils.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTest {

	private MockMvc mockMvc;

	@Mock
	private RegisteredUserService registeredUserServiceMock;

	@Mock
	private Environment environmentMock;

	@InjectMocks
	private RegistrationController registrationControllerUnderTest;

	@Before
	public void setUp() {

		Mockito.reset(registeredUserServiceMock);

		mockMvc = MockMvcBuilders.standaloneSetup(registrationControllerUnderTest).build();
		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void registerForSale_ShouldReturnSuccessMessage() throws Exception {

		UserRegistrationAndPurchaseDTO userRegistrationDTO = new UserRegistrationAndPurchaseDTO(1, 1);

		MessageDTO messageDTO = new MessageDTO(
				"Successfully registered user for sale. You will receive a confirmation email shortly");

		when(registeredUserServiceMock.registerForSale(any(UserRegistrationAndPurchaseDTO.class)))
				.thenReturn(messageDTO);

		mockMvc.perform(post("/registration/for-sale").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtils.convertObjectToJsonBytes(userRegistrationDTO))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(jsonPath("$.message",
						is("Successfully registered user for sale. You will receive a confirmation email shortly")));

		ArgumentCaptor<UserRegistrationAndPurchaseDTO> dtoCaptor = ArgumentCaptor
				.forClass(UserRegistrationAndPurchaseDTO.class);
		verify(registeredUserServiceMock, times(1)).registerForSale(dtoCaptor.capture());
		verifyNoMoreInteractions(registeredUserServiceMock);

		UserRegistrationAndPurchaseDTO dtoArgument = dtoCaptor.getValue();
		assertThat(dtoArgument.getUserId(), is(1));
		assertThat(dtoArgument.getProductId(), is(1));
	}

}
