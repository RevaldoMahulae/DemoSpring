package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.util.ResponseWrapper;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;  // No change here

	@GetMapping
	public List<User> getUsers(){
		return userService.getAllUsers();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseWrapper> getUserById(@PathVariable Long id) {
		ResponseWrapper response = userService.findUserById(id);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/create")
	public ResponseEntity<Object> createUser(@RequestBody User user) {
		System.out.println("Received POST request to /users/create");
		User saveUser = userService.saveUser(user);
		return ResponseEntity.ok(saveUser);
	}

}
