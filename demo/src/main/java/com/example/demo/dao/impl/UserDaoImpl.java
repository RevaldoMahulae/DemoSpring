package com.example.demo.dao.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import com.example.demo.dao.UserDao;
import com.example.demo.model.Division;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.util.Constants;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.DataException;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
            String queryString = String.format(Constants.QUERY_GET_ALL_USERS, sortBy, direction.name());
            NativeQuery<User> sqlQuery = session.createNativeQuery(queryString)
                    .setTupleTransformer(Transformers.aliasToBean(User.class));
            return sqlQuery.getResultList();
        }
    }

    @Override
    public User findUserById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            NativeQuery<User> sqlQuery = session.createNativeQuery(Constants.QUERY_FIND_USER_BY_ID)
                    .setParameter(Constants.PARAM_ID, id)
                    .setTupleTransformer(Transformers.aliasToBean(User.class));
            return sqlQuery.uniqueResult();
        }
    }

    @Override
    public User saveUser(User user, List<Long> roleIds, List<Long> divisionIds) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Long userId = ((Number) session.createNativeQuery(Constants.QUERY_INSERT_USER)
                    .setParameter(Constants.PARAM_NAME, user.getName())
                    .setParameter(Constants.PARAM_EMAIL, user.getEmail())
                    .setParameter(Constants.PARAM_NIK, user.getNik())
                    .setParameter(Constants.PARAM_DOB, user.getDob())
                    .uniqueResult()).longValue();

            for (Long roleId : roleIds) {
                session.createNativeQuery(Constants.QUERY_INSERT_USER_ROLE)
                        .setParameter(Constants.PARAM_USER_ID, userId)
                        .setParameter("roleId", roleId)
                        .executeUpdate();
            }

            for (Long divisionId : divisionIds) {
                session.createNativeQuery(Constants.QUERY_INSERT_USER_DIVISION)
                        .setParameter(Constants.PARAM_USER_ID, userId)
                        .setParameter("divisionId", divisionId)
                        .executeUpdate();
            }

            transaction.commit();
            user.setId(userId);
            return user;
        }
    }

    @Override
    public User updateUser(Long id, User updatedUser, List<Long> roleIds, List<Long> divisionIds) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            int updatedRows = session.createNativeQuery(Constants.QUERY_UPDATE_USER)
                    .setParameter(Constants.PARAM_NAME, updatedUser.getName())
                    .setParameter(Constants.PARAM_EMAIL, updatedUser.getEmail())
                    .setParameter(Constants.PARAM_NIK, updatedUser.getNik())
                    .setParameter(Constants.PARAM_DOB, updatedUser.getDob())
                    .setParameter(Constants.PARAM_ID, id)
                    .executeUpdate();

            session.createNativeQuery(Constants.QUERY_DELETE_USER_ROLE)
                    .setParameter(Constants.PARAM_ID, id)
                    .executeUpdate();

            for (Long roleId : roleIds) {
                session.createNativeQuery(Constants.QUERY_INSERT_USER_ROLE)
                        .setParameter(Constants.PARAM_USER_ID, id)
                        .setParameter("roleId", roleId)
                        .executeUpdate();
            }

            session.createNativeQuery(Constants.QUERY_DELETE_USER_DIVISION)
                    .setParameter(Constants.PARAM_ID, id)
                    .executeUpdate();

            for (Long divisionId : divisionIds) {
                session.createNativeQuery(Constants.QUERY_INSERT_USER_DIVISION)
                        .setParameter(Constants.PARAM_USER_ID, id)
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
            throw new DataAccessResourceFailureException(Constants.ERROR_UPDATING_USER, e);
        }
    }

    @Override
    public Boolean deleteUser(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            int updatedRows = session.createQuery(Constants.QUERY_SOFT_DELETE_USER)
                    .setParameter(Constants.PARAM_ID, id)
                    .executeUpdate();

            transaction.commit();
            return updatedRows > 0;
        }
    }

    @Override
    public List<String> getUserRoles(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createNativeQuery(Constants.QUERY_GET_USER_ROLES)
                .setParameter(Constants.PARAM_USER_ID, userId)
                .getResultList();
        }
    }

    @Override
    public List<String> getUserDivisions(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createNativeQuery(Constants.QUERY_GET_USER_DIVISIONS)
                .setParameter(Constants.PARAM_USER_ID, userId)
                .getResultList();
        }
    }

    @Override
    public boolean restoreUser(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            int updatedRows = session.createQuery(Constants.QUERY_RESTORE_USER)
                    .setParameter(Constants.PARAM_ID, id)
                    .executeUpdate();

            transaction.commit();
            return updatedRows > 0;
        }
    }

    @Override
    public List<Role> getAllRoles(String sortBy, Direction direction) {
        try (Session session = sessionFactory.openSession()) {
            String queryString = String.format(Constants.QUERY_GET_ALL_ROLES, sortBy, direction.name());
            NativeQuery<Role> sqlQuery = session.createNativeQuery(queryString)
                    .addScalar("id", StandardBasicTypes.LONG)
                    .addScalar("roleName", StandardBasicTypes.STRING)
                    .setTupleTransformer(Transformers.aliasToBean(Role.class));
            return sqlQuery.getResultList();
        }
    }


    @Override
    public List<Division> getAllDivisions(String sortBy, Direction direction) {
        try (Session session = sessionFactory.openSession()) {
            String queryString = String.format(Constants.QUERY_GET_ALL_DIVISIONS, sortBy, direction.name());
            NativeQuery<Division> sqlQuery = session.createNativeQuery(queryString)
                    .addScalar("id", StandardBasicTypes.LONG)
                    .addScalar("divisionName", StandardBasicTypes.STRING)
                    .setTupleTransformer(Transformers.aliasToBean(Division.class));
            return sqlQuery.getResultList();
        }
    }

    @Override
    public List<User> searchUsers(String keyword) {
        try (Session session = sessionFactory.openSession()) {
            String sql = "SELECT u.id, u.name, u.email, u.nik, u.dob " +
                         "FROM users u " +
                         "WHERE LOWER(u.name) LIKE LOWER(:keyword) " +
                         "OR LOWER(u.email) LIKE LOWER(:keyword) " +
                         "OR CAST(u.nik AS TEXT) LIKE :keyword " +
                         "AND is_deleted = false " +
                         "ORDER BY u.id ASC";

            NativeQuery<User> sqlQuery = session.createNativeQuery(sql)
                    .addScalar("id", StandardBasicTypes.LONG)
                    .addScalar("name", StandardBasicTypes.STRING)
                    .addScalar("email", StandardBasicTypes.STRING)
                    .addScalar("nik", StandardBasicTypes.INTEGER)
                    .addScalar("dob", StandardBasicTypes.DATE)
                    .setParameter("keyword", "%" + keyword + "%")
                    .setTupleTransformer(Transformers.aliasToBean(User.class));

            return sqlQuery.getResultList();
        }
    }

}
