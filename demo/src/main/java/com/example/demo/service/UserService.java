package com.example.demo.service;

import java.util.List;
import com.example.demo.model.User;
import com.example.demo.util.ResponseWrapper;
import com.example.demo.model.User;

public interface UserService {
    List<User> getAllUsers();
    ResponseWrapper findUserById(Long id);
    User saveUser(User user);
    ResponseWrapper updateUser(Long id, User updatedUser);
    ResponseWrapper deleteUser(Long id);
}