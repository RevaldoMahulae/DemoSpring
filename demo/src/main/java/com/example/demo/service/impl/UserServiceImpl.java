package com.example.demo.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.example.demo.service.UserService;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.example.demo.dao.UserDao;
import com.example.demo.model.User;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public User findUserById(Long id) {
        return userDao.findUserById(id);
    }

    @Override
    public User saveUser(User user, List<Long> roleIds, List<Long> divisionIds) {
        try {
            System.out.println("Saving user with DOB: " + user.getDob()); // Debugging
            return userDao.saveUser(user, roleIds, divisionIds);
        } catch (DataAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error creating user", e);
        }
    }


    @Override
    public User updateUser(Long id, User updatedUser) {
        User user = userDao.updateUser(id, updatedUser);
        if (user == null) {
            throw new RuntimeException("User with ID " + id + " not found");
        }
        return user;
    }

    @Override
    public boolean deleteUser(Long id) {
        return userDao.deleteUser(id);
    }

    @Override
    public List<String> getUserRoles(Long userId) {
        return userDao.getUserRoles(userId);
    }

    @Override
    public List<String> getUserDivisions(Long userId) {
        return userDao.getUserDivisions(userId);
    }
}
