package com.example.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.findUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<String> roles = userService.getUserRoles(id);
        List<String> divisions = userService.getUserDivisions(id);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("id", user.getId());
        responseData.put("name", user.getName());
        responseData.put("email", user.getEmail());
        responseData.put("nik", user.getNik());
        responseData.put("dob", user.getDob());
        responseData.put("role", String.join(",", roles));
        responseData.put("division", String.join(",", divisions));

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody Map<String, Object> requestData) {
        try {
            String name = requestData.get("name").toString();
            String email = requestData.get("email").toString();
            Integer nik = Integer.parseInt(requestData.get("nik").toString());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dob = null;

            if (requestData.get("dob") != null) {
                try {
                    dob = dateFormat.parse(requestData.get("dob").toString());
                } catch (ParseException e) {
                    return ResponseEntity.badRequest().body(null);
                }
            }

            List<Long> roleIds = (List<Long>) requestData.get("roleIds");
            List<Long> divisionIds = (List<Long>) requestData.get("divisionIds");

            User user = new User(name, email, nik, dob);
            User savedUser = userService.saveUser(user, roleIds, divisionIds);

            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
    	User updatedUser = userService.updateUser(id, user);
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean isDeleted = userService.deleteUser(id);
        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("User with ID " + id + " deleted successfully");
    }
}
