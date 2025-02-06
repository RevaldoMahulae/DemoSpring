package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	    User user = userService.findUserById(id);
	    
	    if (user != null) {
	        List<String> roles = userService.getUserRoles(id);
	        List<String> divisions = userService.getUserDivisions(id);
	        
	        String rolesStr = String.join(",", roles);
	        String divisionsStr = String.join(",", divisions);
	        
	        Map<String, Object> responseData = new HashMap<>();
	        responseData.put("name", user.getName());
	        responseData.put("role", rolesStr);
	        responseData.put("division", divisionsStr);
	        
	        ResponseWrapper response = new ResponseWrapper(1, user.getName() + " info retrieved successfully", responseData);
	        return ResponseEntity.ok(response);
	    } else {
	        return ResponseEntity.ok(new ResponseWrapper(0, "User not found", null));
	    }
	}

    @PostMapping("/create")
    public ResponseEntity<User> createUser(
            @RequestBody User user,
            @RequestParam List<Long> roleIds,
            @RequestParam List<Long> divisionIds) {

        User savedUser = userService.saveUser(user, roleIds, divisionIds);
        return ResponseEntity.ok(savedUser);
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
