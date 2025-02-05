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
	private UserService userService;

	//***Old Code
	//	@GetMapping
	//	public List<User> getUsers(){
	//		return userService.getAllUsers();
	//	}
	//
	//	@GetMapping("/{id}")
	//	public ResponseEntity<ResponseWrapper> getUserById(@PathVariable Long id) {
	//		ResponseWrapper response = userService.findUserById(id);
	//		return ResponseEntity.ok(response);
	//	}
	//

	//***New Code

	@GetMapping
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = userService.getAllUsers();
		return ResponseEntity.ok(users);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseWrapper> getUserById(@PathVariable Long id) {
		ResponseWrapper response = userService.findUserById(id);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/create")
	public ResponseEntity<Object> createUser(@RequestBody User user) {
		User saveUser = userService.saveUser(user);
		return ResponseEntity.ok(saveUser);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ResponseWrapper> updateUser(@PathVariable Long id, @RequestBody User user) {
		ResponseWrapper updatedUser = userService.updateUser(id, user);
		return ResponseEntity.ok(updatedUser);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable Long id) {
		ResponseWrapper response = userService.deleteUser(id);
		return ResponseEntity.ok(response);
	}

}
