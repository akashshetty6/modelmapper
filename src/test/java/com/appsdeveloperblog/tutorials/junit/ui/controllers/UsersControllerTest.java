package com.appsdeveloperblog.tutorials.junit.ui.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.appsdeveloperblog.tutorials.junit.service.UsersService;
import com.appsdeveloperblog.tutorials.junit.shared.UserDto;
import com.appsdeveloperblog.tutorials.junit.ui.request.UserDetailsRequestModel;
import com.appsdeveloperblog.tutorials.junit.ui.response.UserRest;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers =UsersController.class,
excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class UsersControllerTest {
	
	@MockBean
	UsersService service;
	
	@Autowired
	MockMvc mockMvc;

	@Test
	@DisplayName("user can be created")
	void testCreateUser_whenValidInputs_returnCreatedUserDetails() throws Exception {
		UserDetailsRequestModel detailsRequestModel=new UserDetailsRequestModel();
		detailsRequestModel.setFirstName("akash");
		detailsRequestModel.setLastName("shetty");
		detailsRequestModel.setEmail("akash@gmil.com");
		detailsRequestModel.setPassword("akash@123");
		detailsRequestModel.setRepeatPassword("akash@123");
		
		
		UserDto dto=new ModelMapper().map(detailsRequestModel, UserDto.class);
		dto.setUserId(UUID.randomUUID().toString());
		when(service.createUser(any(UserDto.class))).thenReturn(dto);
		
		
		
		RequestBuilder builder= MockMvcRequestBuilders.post("/users")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(new ObjectMapper().writeValueAsString(detailsRequestModel));
	
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		String result=mvcResult.getResponse().getContentAsString();
		UserRest readValue = new ObjectMapper().readValue(result, UserRest.class);
		
		assertEquals(detailsRequestModel.getFirstName(),readValue.getFirstName());
		
	}
	    
}
