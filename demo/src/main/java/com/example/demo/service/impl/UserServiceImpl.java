package com.example.demo.service.impl;

import java.util.List;

import com.example.demo.service.UserService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
    public User saveUser(User user) {
        try{
            return userDao.saveUser(user);
        } catch (DataAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error creating user", e);
        }
    }

	@Override
	public ResponseWrapper updateUser(Long id, User updatedUser) {
		User user = userDao.updateUser(id, updatedUser);
	    if (user != null) {
	        return new ResponseWrapper(1, user.getName() + " updated successfully", user);
	    } else {
	        return new ResponseWrapper(0, "User with ID " + id + " not found", null);
	    }
	}

	@Override
	public ResponseWrapper deleteUser(Long id) {
		 boolean isDeleted = userDao.deleteUser(id);
		    if (isDeleted) {
		        return new ResponseWrapper(1, "User with ID " + id + " deleted successfully", null);
		    } else {
		        return new ResponseWrapper(0, "User with ID " + id + " not found", null);
		    }
	}
}
