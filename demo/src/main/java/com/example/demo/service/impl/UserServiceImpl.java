package com.example.demo.service.impl;

import java.util.List;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dao.UserDao;
import com.example.demo.model.User;
import com.example.demo.util.ResponseWrapper;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public ResponseWrapper findUserById(Long id) {
        User user = userDao.findUserById(id);
        if (user != null) {
            return new ResponseWrapper(1, user.getName() + " info retrieved successfully", user);
        } else {
            return new ResponseWrapper(0, "User with ID " + id + " not found", null);
        }
    }

    @Override
    public void saveUser(User user) {
        userDao.saveUser(user);
    }
}
