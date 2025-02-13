package com.example.demo.dao.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import com.example.demo.dao.UserDao;
import com.example.demo.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.DataException;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getAllUsers(String sortBy, Sort.Direction direction) {
        try (Session session = sessionFactory.openSession()) {
            String queryString = "SELECT id, name, email, nik, dob FROM users ORDER BY " + sortBy + " " + direction.name();
            
            NativeQuery<User> sqlQuery = session.createNativeQuery(queryString)
            		.setTupleTransformer(Transformers.aliasToBean(User.class));
            
            List<User> users = sqlQuery.getResultList();
            return users;
        }catch(Exception e) {
        	throw new DataAccessException("Error fetching all users", e){
			};
        }
    }

    @Override
    public User findUserById(Long id) {
        try (Session session = sessionFactory.openSession()) {
        	String queryString = "SELECT id, name, email, nik, dob FROM users WHERE id = :id";
        	
        	NativeQuery<User> sqlQuery = session.createNativeQuery(queryString)
        			.setParameter("id", id)
        			.setTupleTransformer(Transformers.aliasToBean(User.class));
        	
            User user = sqlQuery.uniqueResult();
            return user;
        }catch(Exception e) {
        	throw new DataAccessException("Error fetching data", e){
			};
        }
    }

    @Override
    public User saveUser(User user, List<Long> roleIds, List<Long> divisionIds) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Long userId = ((Number) session.createNativeQuery(
                    "INSERT INTO users (name, email, nik, dob) VALUES (:name, :email, :nik, :dob) RETURNING id")
                    .setParameter("name", user.getName())
                    .setParameter("email", user.getEmail())
                    .setParameter("nik", user.getNik())
                    .setParameter("dob", user.getDob())
                    .uniqueResult())
                    .longValue();

            if (userId == null) {
                throw new RuntimeException("User ID not generated");
            }


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
    public User updateUser(Long id, User updatedUser, List<Long> roleIds, List<Long> divisionIds) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            int updatedRows = session.createNativeQuery(
                    "UPDATE users SET name = :name, email = :email, nik = :nik, dob = :dob WHERE id = :id")
                    .setParameter("name", updatedUser.getName())
                    .setParameter("email", updatedUser.getEmail())
                    .setParameter("nik", updatedUser.getNik())
                    .setParameter("dob", updatedUser.getDob())
                    .setParameter("id", id)
                    .executeUpdate();

            session.createNativeQuery("DELETE FROM user_roles WHERE user_id = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            for (Long roleId : roleIds) {
                session.createNativeQuery("INSERT INTO user_roles (user_id, role_id) VALUES (:userId, :roleId)")
                        .setParameter("userId", id)
                        .setParameter("roleId", roleId)
                        .executeUpdate();
            }

            session.createNativeQuery("DELETE FROM user_divisions WHERE user_id = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            for (Long divisionId : divisionIds) {
                session.createNativeQuery("INSERT INTO user_divisions (user_id, division_id) VALUES (:userId, :divisionId)")
                        .setParameter("userId", id)
                        .setParameter("divisionId", divisionId)
                        .executeUpdate();
            }

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
