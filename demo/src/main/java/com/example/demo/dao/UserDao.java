package com.example.demo.dao;

import java.util.List;
import com.example.demo.model.User;

public interface UserDao {
    List<User> getAllUsers();
    User findUserById(Long id);
    User saveUser(User user);
    User updateUser(Long id, User updatedUser);
    Boolean deleteUser(Long id);
}
