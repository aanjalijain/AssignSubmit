package com.uxpsystems.assignment.service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uxpsystems.assignment.dao.UserRepository;
import com.uxpsystems.assignment.exception.RecordNotFoundException;
import com.uxpsystems.assignment.model.UserDetail;

@Service
public class UserService {

	 @Autowired
	 UserRepository repository;
	 
	 public List<UserDetail> getAllUsers()
	    {
	        List<UserDetail> userList = repository.findAll();
	        
	        if(userList.size() > 0) {
	            return userList;
	        } else {
	            return new ArrayList<UserDetail>();
	        }
	    }
	     
	    public UserDetail getUserById(Long id) throws RecordNotFoundException
	    {
	    	 Optional<UserDetail> user = repository.findById(id);
	         
	        if(user.isPresent()) {
	            return user.get();
	        } else {
	            throw new RecordNotFoundException("No User record exist for given id");
	        }
	    }
	     
	    public UserDetail createUser(UserDetail entity) throws RecordNotFoundException
	    {
	    	
	            entity = repository.save(entity);
	             
	            return entity;
	      //  }
	    }
	    
	    public UserDetail updateUser(UserDetail entity,long id) throws RecordNotFoundException{
	    	Optional<UserDetail> user = repository.findById(id);
	    	if(user.isPresent())
	        {
	            UserDetail newEntity = user.get();
	            newEntity.setUserName(entity.getUserName());
	            newEntity.setUserPassword(entity.getUserPassword());
	            newEntity.setUserStatus(entity.getUserStatus());
	 
	            newEntity = repository.save(newEntity);
	             
	            return newEntity;
	        } 
	    	 return entity;
	    }
	     
	    public void deleteUserById(Long id) throws RecordNotFoundException
	    {
	        Optional<UserDetail> User = repository.findById(id);
	         
	        if(User.isPresent())
	        {
	            repository.deleteById(id);
	        } else {
	            throw new RecordNotFoundException("No User record exist for given id");
	        }
	    }
}
