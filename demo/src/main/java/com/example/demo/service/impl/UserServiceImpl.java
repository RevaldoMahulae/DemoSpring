package com.example.demo.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.service.UserService;

import jakarta.transaction.Transactional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.demo.dao.UserDao;
import com.example.demo.model.User;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public List<User> getAllUsers(String sortBy, Sort.Direction direction) {
        return userDao.getAllUsers(sortBy, direction);
    }

    @Override
    public User findUserById(Long id) {
        return userDao.findUserById(id);
    }
    
    @Override
    public Map<String, Object> getUserDetails(Long id) {
        User user = findUserById(id);
        if (user == null) {
            return null;
        }

        List<String> roles = getUserRoles(id);
        List<String> divisions = getUserDivisions(id);


        Map<String, Object> responseData = new HashMap<>();
        responseData.put("id", user.getId());
        responseData.put("name", user.getName());
        responseData.put("email", user.getEmail());
        responseData.put("nik", user.getNik());
        responseData.put("dob", user.getDob());
        responseData.put("role", String.join(",", roles));
        responseData.put("division", String.join(",", divisions));

        return responseData;
    }


    @Override
    public User saveUser(User user, List<Long> roleIds, List<Long> divisionIds) {
        try {
            return userDao.saveUser(user, roleIds, divisionIds);
        } catch (DataAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error creating user", e);
        }
    }


    @Override
    public User updateUser(Long id, User updatedUser, List<Long> roleIds, List<Long> divisionIds) {
        User user = userDao.updateUser(id, updatedUser, roleIds, divisionIds);
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
    
    @Override
    public boolean restoreUser(Long id) {
        return userDao.restoreUser(id);
    }

}
