package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;

import com.example.demo.model.User;
import com.example.demo.util.ResponseWrapper;
import com.example.demo.model.User;

public interface UserService {
	List<User> getAllUsers(String sortBy, Sort.Direction direction);
    User findUserById(Long id);
    User saveUser(User user, List<Long> roleIds, List<Long> divisionIds);
    User updateUser(Long id, User updatedUser, List<Long> roleIds, List<Long> divisionIds);
    boolean deleteUser(Long id);
    List<String> getUserRoles(Long userId);
    List<String> getUserDivisions(Long userId);
	Map<String, Object> getUserDetails(Long id);
	boolean restoreUser(Long id);
}