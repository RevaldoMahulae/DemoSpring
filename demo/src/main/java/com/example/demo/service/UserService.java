package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.ResponseWrapper;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public List<User> getAllUsers(){
		return userRepository.findAll();
	}
	
	 public ResponseWrapper findUserById(Long id) {
	        Optional<User> user = userRepository.findById(id);
	        if (user.isPresent()) {
	            User foundUser = user.get();
	            return new ResponseWrapper(1, foundUser.getName() + " info retrieved successfully", foundUser);
	        } else {
	            return new ResponseWrapper(0, "User with ID " + id + " not found", null);
	        }
	    }
	
	public User saveUser(User user) {
		return userRepository.save(user);
	}

}
