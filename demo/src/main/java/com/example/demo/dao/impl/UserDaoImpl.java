package com.example.demo.dao.impl;

import java.util.List;
import java.util.ArrayList;

import com.example.demo.dao.UserDao;
import com.example.demo.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            List<Object[]> results = session.createNativeQuery(
                "SELECT id, name, email, nik FROM users").getResultList();
            
            List<User> users = new ArrayList<>();
            for (Object[] row : results) {
                User user = new User();
                user.setId(((Number) row[0]).longValue());
                user.setName((String) row[1]);
                user.setEmail((String) row[2]);
                user.setNik(((Number) row[3]).intValue());
                users.add(user);
            }
            return users;
        }
    }

    @Override
    public User findUserById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Object[] result = (Object[]) session.createNativeQuery(
                "SELECT id, name, email, nik FROM users WHERE id = :id")
                .setParameter("id", id)
                .uniqueResult();
            
            if (result != null) {
                User user = new User();
                user.setId(((Number) result[0]).longValue());
                user.setName((String) result[1]);
                user.setEmail((String) result[2]);
                user.setNik(((Number) result[3]).intValue());
                return user;
            }
            return null;
        }
    }

    @Override
    public User saveUser(User user, List<Long> roleIds, List<Long> divisionIds) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            int insertedRows = session.createNativeQuery(
                "INSERT INTO users (name, email, nik) VALUES (:name, :email, :nik)")
                .setParameter("name", user.getName())
                .setParameter("email", user.getEmail())
                .setParameter("nik", user.getNik())
                .executeUpdate();

            if (insertedRows == 0) {
                throw new RuntimeException("User not inserted");
            }

            Long userId = ((Number) session.createNativeQuery("SELECT LAST_INSERT_ID()")
                    .uniqueResult()).longValue();

            for (Long roleId : roleIds) {
                session.createNativeQuery("INSERT INTO user_roles (user_id, role_id) VALUES (:userId, :roleId)")
                        .setParameter("userId", userId)
                        .setParameter("roleId", roleId)
                        .executeUpdate();
            }

            for (Long divisionId : divisionIds) {
                session.createNativeQuery("INSERT INTO user_divisions (user_id, division_id) VALUES (:userId, :divisionId)")
                        .setParameter("userId", userId)
                        .setParameter("divisionId", divisionId)
                        .executeUpdate();
            }

            transaction.commit();
            user.setId(userId);
            return user;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new DataAccessResourceFailureException("Error saving user", e);
        }
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            int updatedRows = session.createNativeQuery(
                    "UPDATE users SET name = :name, email = :email, nik = :nik WHERE id = :id")
                    .setParameter("name", updatedUser.getName())
                    .setParameter("email", updatedUser.getEmail())
                    .setParameter("nik", updatedUser.getNik())
                    .setParameter("id", id)
                    .executeUpdate();

            transaction.commit();

            if (updatedRows > 0) {
                return findUserById(id);
            }
            return null;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new DataAccessResourceFailureException("Error updating user", e);
        }
    }

    @Override
    public Boolean deleteUser(Long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            int deletedRows = session.createNativeQuery("DELETE FROM users WHERE id = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            transaction.commit();
            return deletedRows > 0;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new DataAccessResourceFailureException("Error deleting user", e);
        }
    }

    @Override
    public List<String> getUserRoles(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createNativeQuery(
                    "SELECT r.role_name FROM roles r " +
                    "JOIN user_roles ur ON r.id = ur.role_id " +
                    "WHERE ur.user_id = :userId")
                .setParameter("userId", userId)
                .getResultList();
        }
    }

    @Override
    public List<String> getUserDivisions(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createNativeQuery(
                    "SELECT d.division_name FROM divisions d " +
                    "JOIN user_divisions ud ON d.id = ud.division_id " +
                    "WHERE ud.user_id = :userId")
                .setParameter("userId", userId)
                .getResultList();
        }
    }
}
