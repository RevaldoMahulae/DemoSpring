package com.example.demo.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.example.demo.model.Division;
import com.example.demo.model.Role;
import com.example.demo.model.User;

public interface UserDao {
	List<User> getAllUsers(String sortBy, Sort.Direction direction);
    User findUserById(Long id);
    User saveUser(User user, List<Long> roleIds, List<Long> divisionIds);
    User updateUser(Long id, User updatedUser, List<Long> roleIds, List<Long> divisionIds);
    Boolean deleteUser(Long id);
    List<String> getUserRoles(Long userId);
    List<String> getUserDivisions(Long userId);
	boolean restoreUser(Long id);
	List<Role> getAllRoles(String sortBy, Direction direction);
	List<Division> getAllDivisions(String sortBy, Direction direction);
	List<User> searchUsers(String keyword);
}
