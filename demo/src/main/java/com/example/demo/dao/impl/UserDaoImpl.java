package com.example.demo.dao.impl;

import java.util.List;

import com.example.demo.dao.UserDao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Division;
import com.example.demo.model.Role;
import com.example.demo.model.User;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    private final SessionFactory sessionFactory;

    private final String WORKCENTER = "Workcenter(s)";
    @Autowired
    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public User findUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User saveUser(User user, List<Long> roleIds, List<Long> divisionIds) {
        entityManager.persist(user);
        entityManager.flush();

        Long userId = user.getId();

        for (Long roleId : roleIds) {
            entityManager.createQuery("INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)")
                    .setParameter(1, userId)
                    .setParameter(2, roleId)
                    .executeUpdate();
        }

        for (Long divisionId : divisionIds) {
            entityManager.createQuery("INSERT INTO user_divisions (user_id, division_id) VALUES (?, ?)")
                    .setParameter(1, userId)
                    .setParameter(2, divisionId)
                    .executeUpdate();
        }

        return user;
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        try {
            int updatedRows = entityManager.createQuery(
                    "UPDATE users SET name = ?, email = ?, nik = ? WHERE id = ?")
                    .setParameter(1, updatedUser.getName())
                    .setParameter(2, updatedUser.getEmail())
                    .setParameter(3, updatedUser.getNik())
                    .setParameter(4, id)
                    .executeUpdate();

            if (updatedRows > 0) {
                return findUserById(id);
            }
            return null;
        } catch (Exception e) {
            throw new DataAccessResourceFailureException("Error updating user", e);
        }
    }


    @Override
    public Boolean deleteUser(Long id) {
        try {
            int deletedRows = entityManager.createQuery(
                    "DELETE FROM users WHERE id = ?")
                    .setParameter(1, id)
                    .executeUpdate();

            return deletedRows > 0;
        } catch (Exception e) {
            throw new DataAccessResourceFailureException("Error deleting user", e);
        }
    }

    @Override
    public List<String> getUserRoles(Long userId) {
        return entityManager.createNativeQuery(
                "SELECT r.role_name FROM roles r " +
                "JOIN user_roles ur ON r.id = ur.role_id " +
                "WHERE ur.user_id = ?")
            .setParameter(1, userId)
            .getResultList()
            .stream()
            .map(Object::toString)  
            .toList(); 
    }

    @Override
    public List<String> getUserDivisions(Long userId) {
        return entityManager.createNativeQuery(
                "SELECT d.division_name FROM divisions d " +
                "JOIN user_divisions ud ON d.id = ud.division_id " +
                "WHERE ud.user_id = ?")
            .setParameter(1, userId)
            .getResultList()
            .stream()
            .map(Object::toString)  
            .toList(); 
    }




}
