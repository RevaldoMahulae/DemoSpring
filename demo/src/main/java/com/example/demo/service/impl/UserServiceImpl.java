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
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import com.example.demo.dao.UserDao;
import com.example.demo.model.Division;
import com.example.demo.model.Role;
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
        if (user == null) {
            throw new IllegalArgumentException("User object cannot be null");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be null or empty");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("User email cannot be null or empty");
        }
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("User must have at least one role");
        }
        if (divisionIds == null || divisionIds.isEmpty()) {
            throw new IllegalArgumentException("User must be assigned to at least one division");
        }

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
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (updatedUser == null) {
            throw new IllegalArgumentException("Updated user data cannot be null");
        }
        if (updatedUser.getName() == null || updatedUser.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be null or empty");
        }
        if (updatedUser.getEmail() == null || updatedUser.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("User email cannot be null or empty");
        }
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("User must have at least one role");
        }
        if (divisionIds == null || divisionIds.isEmpty()) {
            throw new IllegalArgumentException("User must be assigned to at least one division");
        }

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

	@Override
	public List<Role> getAllRoles(String sortBy, Direction direction) {
		return userDao.getAllRoles(sortBy, direction);
	}

	@Override
	public List<Division> getAllDivision(String sortBy, Direction direction) {
		return userDao.getAllDivisions(sortBy, direction);
	}

	@Override
	public List<User> searchUsers(String keyword) {
		return userDao.searchUsers(keyword);
	}
    
    

}
