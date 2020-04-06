package com.uxpsystems.assignment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uxpsystems.assignment.exception.RecordNotFoundException;
import com.uxpsystems.assignment.model.UserDetail;
import com.uxpsystems.assignment.service.UserService;

@RestController
@RequestMapping("assignment/users")
public class UserController {
	
	
	 @Autowired
	 UserService service;
	
	 @GetMapping
	    public ResponseEntity<List<UserDetail>> getAllUsers() {
	        List<UserDetail> list = service.getAllUsers();
	 
	        return new ResponseEntity<List<UserDetail>>(list, new HttpHeaders(), HttpStatus.OK);
	    }
	 
	    @GetMapping("/{id}")
	    public ResponseEntity<UserDetail> getUserById(@PathVariable("id") Long id)
	                                                    throws RecordNotFoundException {
	        UserDetail entity = service.getUserById(id);
	 
	        return new ResponseEntity<UserDetail>(entity, new HttpHeaders(), HttpStatus.OK);
	    }
	 
	    @PostMapping
	    public ResponseEntity<UserDetail> createUser(@RequestBody UserDetail User)
	                                                    throws RecordNotFoundException {
	        UserDetail updated = service.createUser(User);
	        return new ResponseEntity<UserDetail>(updated, new HttpHeaders(), HttpStatus.OK);
	    }
	    
	    @PutMapping("/{id}")
	    public ResponseEntity<UserDetail> updateUser(@PathVariable("id") Long id,@RequestBody UserDetail User)
	                                                    throws RecordNotFoundException {
	        UserDetail updated = service.updateUser(User,id);
	        return new ResponseEntity<UserDetail>(updated, new HttpHeaders(), HttpStatus.OK);
	    }
	 
	    @DeleteMapping("/{id}")
	    public HttpStatus deleteUserById(@PathVariable("id") Long id)
	                                                    throws RecordNotFoundException {
	        service.deleteUserById(id);
	        return HttpStatus.FORBIDDEN;
	    }
	 
	}
