package com.example.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {

        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        List<User> users = userService.getAllUsers(sortBy, direction);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.findUserById(id);
        if (user == null) {
            return ResponseEntity.status(404).body("User tidak ditemukan");
        }
        return ResponseEntity.ok(userService.getUserDetails(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody Map<String, Object> requestData) {
        try {
            String name = requestData.get("name").toString();
            String email = requestData.get("email").toString();
            Integer nik = Integer.parseInt(requestData.get("nik").toString());

            Date dob = null;
            if (requestData.get("dob") != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat.setLenient(false);
                try {
                    dob = dateFormat.parse(requestData.get("dob").toString());
                } catch (ParseException e) {
                    return ResponseEntity.badRequest().body("Format tanggal lahir (dob) harus yyyy-MM-dd.");
                }
            }
            
            List<Long> roleIds = ((List<?>) requestData.get("roleIds"))
                                .stream()
                                .map(o -> Long.parseLong(o.toString()))
                                .toList();

            List<Long> divisionIds = ((List<?>) requestData.get("divisionIds"))
                                    .stream()
                                    .map(o -> Long.parseLong(o.toString()))
                                    .toList();

            User user = new User(name, email, nik, dob);
            User savedUser = userService.saveUser(user, roleIds, divisionIds);

            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Gagal save ser");
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(
        @PathVariable Long id,
        @RequestBody Map<String, Object> requestBody) {
        
        try {
            User user = new User();
            user.setName((String) requestBody.get("name"));
            user.setEmail((String) requestBody.get("email"));
            
            Object nikObj = requestBody.get("nik");
            Integer nik = (nikObj instanceof Number) ? ((Number) nikObj).intValue() : Integer.parseInt(nikObj.toString());
            user.setNik(nik);
            
            if (requestBody.get("dob") != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat.setLenient(false);
                try {
                    user.setDob(dateFormat.parse((String) requestBody.get("dob")));
                } catch (ParseException e) {
                    return ResponseEntity.badRequest().body("Format tanggal lahir (dob) harus yyyy-MM-dd.");
                }
            }

            
            

            List<Long> roleIds = ((List<?>) requestBody.get("roleIds"))
                                .stream()
                                .map(o -> Long.valueOf(o.toString()))
                                .collect(Collectors.toList());

            List<Long> divisionIds = ((List<?>) requestBody.get("divisionIds"))
                                    .stream()
                                    .map(o -> Long.valueOf(o.toString()))
                                    .collect(Collectors.toList());

            User updatedUser = userService.updateUser(id, user, roleIds, divisionIds);

            if (updatedUser == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
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
