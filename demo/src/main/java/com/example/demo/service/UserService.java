package com.example.demo.service;

import java.util.List;
import com.example.demo.model.User;
import com.example.demo.util.ResponseWrapper;
import com.example.demo.model.User;

public interface UserService {
    List<User> getAllUsers();
    User findUserById(Long id);
    User saveUser(User user, List<Long> roleIds, List<Long> divisionIds);
    User updateUser(Long id, User updatedUser);
    boolean deleteUser(Long id);
    List<String> getUserRoles(Long userId);
    List<String> getUserDivisions(Long userId);
}