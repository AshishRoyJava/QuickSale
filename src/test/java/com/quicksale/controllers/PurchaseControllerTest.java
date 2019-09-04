package com.quicksale.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;

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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.quicksale.dtos.MessageDTO;
import com.quicksale.dtos.PurchaseDTO;
import com.quicksale.dtos.UserRegistrationAndPurchaseDTO;
import com.quicksale.services.PurchaseService;
import com.quicksale.utils.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PurchaseControllerTest {

	private MockMvc mockMvc;

	@Mock
	private PurchaseService purchaseServiceMock;

	@InjectMocks
	private PurchaseController purchaseControllerUnderTest;

	@Before
	public void setUp() {

		Mockito.reset(purchaseServiceMock);

		mockMvc = MockMvcBuilders.standaloneSetup(purchaseControllerUnderTest).build();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getAllPurchaseOrders_ShouldReturnFoundPurchaseOrders() throws Exception {

		PurchaseDTO first = new PurchaseDTO("Test1", "Hyderabad", "abc@gmail.com", "TestProduct1", "Details1", 5000.0,
				new Date(System.currentTimeMillis()));
		PurchaseDTO second = new PurchaseDTO("Test2", "Hyderabad", "def@gmail.com", "TestProduct2", "Details2", 2000.0,
				new Date(System.currentTimeMillis()));

		when(purchaseServiceMock.getAllPurchaseOrders()).thenReturn(Arrays.asList(first, second));

		mockMvc.perform(get("/purchases/all")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].userName", is("Test1")))
				.andExpect(jsonPath("$[0].userAddress", is("Hyderabad")))
				.andExpect(jsonPath("$[0].userEmail", is("abc@gmail.com")))
				.andExpect(jsonPath("$[0].productName", is("TestProduct1")))
				.andExpect(jsonPath("$[0].productDescription", is("Details1")))
				.andExpect(jsonPath("$[0].productPrice", is(5000.0)))

				.andExpect(jsonPath("$[1].userName", is("Test2")))
				.andExpect(jsonPath("$[1].userAddress", is("Hyderabad")))
				.andExpect(jsonPath("$[1].userEmail", is("def@gmail.com")))
				.andExpect(jsonPath("$[1].productName", is("TestProduct2")))
				.andExpect(jsonPath("$[1].productDescription", is("Details2")))
				.andExpect(jsonPath("$[1].productPrice", is(2000.0)));

		verify(purchaseServiceMock, times(1)).getAllPurchaseOrders();
		verifyNoMoreInteractions(purchaseServiceMock);

	}

	@Test
	public void purchaseProduct_ShouldReturnSuccessMessage() throws Exception {

		UserRegistrationAndPurchaseDTO userRegistrationDTO = new UserRegistrationAndPurchaseDTO(1, 1);

		MessageDTO messageDTO = new MessageDTO("Order placed Successfully");

		when(purchaseServiceMock.purchaseProduct(any(UserRegistrationAndPurchaseDTO.class))).thenReturn(messageDTO);

		mockMvc.perform(post("/purchases/new").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtils.convertObjectToJsonBytes(userRegistrationDTO))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.message", is("Order placed Successfully")));

		ArgumentCaptor<UserRegistrationAndPurchaseDTO> dtoCaptor = ArgumentCaptor
				.forClass(UserRegistrationAndPurchaseDTO.class);
		verify(purchaseServiceMock, times(1)).purchaseProduct(dtoCaptor.capture());
		verifyNoMoreInteractions(purchaseServiceMock);

		UserRegistrationAndPurchaseDTO dtoArgument = dtoCaptor.getValue();
		assertThat(dtoArgument.getUserId(), is(1));
		assertThat(dtoArgument.getProductId(), is(1));
	}

}
