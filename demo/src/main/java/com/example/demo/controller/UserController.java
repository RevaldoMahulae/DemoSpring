package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.util.ResponseWrapper;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	
	
	@GetMapping
	public List<User> getUsers(){
		return userService.getAllUsers();
	}
	
    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper> getUserById(@PathVariable Long id) {
        ResponseWrapper response = userService.findUserById(id);
        return ResponseEntity.ok(response);
    }
		
	@PostMapping
	public User createUser(@RequestBody User user) {
		return userService.saveUser(user);
	}

}
