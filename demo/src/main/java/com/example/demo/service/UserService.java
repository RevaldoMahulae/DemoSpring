package com.example.demo.service;

import java.util.List;
import com.example.demo.model.User;
import com.example.demo.util.ResponseWrapper;

public interface UserService {
    List<User> getAllUsers();
    ResponseWrapper findUserById(Long id);
    void saveUser(User user);
}