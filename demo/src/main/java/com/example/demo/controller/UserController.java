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
import com.example.demo.service.EmailService;
import com.example.demo.service.UserService;
import com.example.demo.util.Constants;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private EmailService emailService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam(defaultValue = Constants.PARAM_ID) String sortBy,
            @RequestParam(defaultValue = Constants.DEFAULT_DIRECTION) String order) {

        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        List<User> users = userService.getAllUsers(sortBy, direction);
        return ResponseEntity.ok(users);
    }

    @GetMapping(Constants.API_GET_USER_BY_ID)
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.findUserById(id);
        if (user == null) {
            return ResponseEntity.status(404).body(Constants.USER_NOT_FOUND);
        }
        return ResponseEntity.ok(userService.getUserDetails(id));
    }

    @PostMapping(Constants.API_CREATE_USER)
    public ResponseEntity<?> createUser(@RequestBody Map<String, Object> requestData) {
        try {
            String name = requestData.get(Constants.PARAM_NAME).toString();
            String email = requestData.get(Constants.PARAM_EMAIL).toString();
            Integer nik = Integer.parseInt(requestData.get(Constants.PARAM_NIK).toString());

            Date dob = null;
            if (requestData.get(Constants.PARAM_DOB) != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.YEAR_FORMAT);
                dateFormat.setLenient(false);
                try {
                    dob = dateFormat.parse(requestData.get("dob").toString());
                } catch (ParseException e) {
                    return ResponseEntity.badRequest().body(Constants.DOB_FORMAT_ERROR);
                }
            }

            List<Long> roleIds = ((List<?>) requestData.get(Constants.PARAM_ROLE_ID))
                    .stream()
                    .map(o -> Long.parseLong(o.toString()))
                    .toList();

            List<Long> divisionIds = ((List<?>) requestData.get(Constants.PARAM_DIVISION_ID))
                    .stream()
                    .map(o -> Long.parseLong(o.toString()))
                    .toList();

            User user = new User(name, email, nik, dob);
            User savedUser = userService.saveUser(user, roleIds, divisionIds);

            String hrdEmail = Constants.EMAIL_HRD;
            String subject = Constants.EMAIL_SUBJECT_NEW_USER;
            String body = generateEmailContent(savedUser);

            emailService.sendUserCreationEmail(email, hrdEmail, subject, body);

            return ResponseEntity.ok(savedUser);
        } catch (MessagingException e) {
            return ResponseEntity.badRequest().body(Constants.EMAIL_SEND_ERROR);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Constants.SAVE_USER_ERROR);
        }
    }


    private String generateEmailContent(User user) {
        return "<html>" +
                "<body>" +
                "<h2>Data User Baru</h2>" +
                "<p><strong>Nama:</strong> " + user.getName() + "</p>" +
                "<p><strong>Email:</strong> " + user.getEmail() + "</p>" +
                "<p><strong>NIK:</strong> " + user.getNik() + "</p>" +
                "<p><strong>Tanggal Lahir:</strong> " + user.getDob() + "</p>" +
                "<br>" +
                "<p>Terima kasih.</p>" +
                "</body>" +
                "</html>";
    }

	@PutMapping(Constants.API_UPDATE_USER)
    public ResponseEntity<?> updateUser(
        @PathVariable Long id,
        @RequestBody Map<String, Object> requestBody) {
        
        try {
            User user = new User();
            user.setName((String) requestBody.get(Constants.PARAM_NAME));
            user.setEmail((String) requestBody.get(Constants.PARAM_EMAIL));
            
            Object nikObj = requestBody.get(Constants.PARAM_NIK);
            Integer nik = (nikObj instanceof Number) ? ((Number) nikObj).intValue() : Integer.parseInt(nikObj.toString());
            user.setNik(nik);
            
            if (requestBody.get("dob") != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.YEAR_FORMAT);
                dateFormat.setLenient(false);
                try {
                    user.setDob(dateFormat.parse((String) requestBody.get(Constants.PARAM_DOB)));
                } catch (ParseException e) {
                    return ResponseEntity.badRequest().body(Constants.DOB_FORMAT_ERROR);
                }
            }

            
            

            List<Long> roleIds = ((List<?>) requestBody.get(Constants.PARAM_ROLE_ID))
                                .stream()
                                .map(o -> Long.valueOf(o.toString()))
                                .collect(Collectors.toList());

            List<Long> divisionIds = ((List<?>) requestBody.get(Constants.PARAM_DIVISION_ID))
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


    @DeleteMapping(Constants.API_DELETE_USER)
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean isDeleted = userService.deleteUser(id);
        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("User with ID " + id + " deleted successfully");
    }
    
    @PutMapping(Constants.API_RESTORE_USER)
    public ResponseEntity<String> restoreUser(@PathVariable Long id) {
        boolean isRestored = userService.restoreUser(id);
        if (!isRestored) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("User with ID " + id + " restored successfully");
    }

}
