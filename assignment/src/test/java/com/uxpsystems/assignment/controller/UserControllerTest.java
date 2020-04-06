package com.uxpsystems.assignment.controller;

import com.uxpsystems.assignment.exception.RecordNotFoundException;
import com.uxpsystems.assignment.model.UserDetail;
import com.uxpsystems.assignment.test.config.TestBeanConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


import static org.junit.Assert.*;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestBeanConfig.class})
public class UserControllerTest {
	
	@Autowired
	private UserController userController;


	
	@Test
	public void listAllUsers_Test() {

		ResponseEntity<?> result =  userController.getAllUsers();
		assertNotNull(result);
		
	}

	@Test
	public void deleteUser_Test() throws RecordNotFoundException {

		HttpStatus result =  userController.deleteUserById(2L);
		assertNotNull(result);
		equals(HttpStatus.OK);
	}
	
	@Test
	public void getUser_Test() throws RecordNotFoundException {

		ResponseEntity<UserDetail> result =  userController.getUserById(3L);
	    assertNotNull(result);
		equals(HttpStatus.OK);
	}
	
	@Test
	public void updateUser_Test() throws RecordNotFoundException {
		UserDetail user=new UserDetail();
		user.setUserName("John");
		user.setUserPassword("Test@1234");
		user.setUserStatus("Deactive");
		ResponseEntity<UserDetail> result =  userController.updateUser(1L,user);
		assertNotNull(result);
		assertEquals(result.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void addNewUser_Test() throws RecordNotFoundException {
		UserDetail user=new UserDetail();
		user.setUserName("John");
		user.setUserPassword("Test@1234");
		user.setUserStatus("Deactive");
		ResponseEntity<?> result =  userController.createUser(user);
		assertNotNull(result);
		assertEquals(result.getStatusCode(), HttpStatus.OK);
	}
	
	
}