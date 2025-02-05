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
    public User saveUser(User user) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();


            String insertQuerySQL = "INSERT INTO users (name, nik, email) VALUES (:name, :nik, :email)";

            NativeQuery insertQuery = session.createNativeQuery(insertQuerySQL);
            insertQuery.setParameter("name", user.getName());
            insertQuery.setParameter("nik", user.getNik());
            insertQuery.setParameter("email", user.getEmail());

            insertQuery.executeUpdate();

            tx.commit();
            return user;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new DataAccessResourceFailureException("Error creating user", e);
        }
    }

	@Override
	public User updateUser(Long id, User updatedUser) {
		Transaction tx = null;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
	        
	        User existingUser = session.get(User.class, id);
	        if (existingUser != null) {
	            existingUser.setName(updatedUser.getName());
	            existingUser.setEmail(updatedUser.getEmail());
	            existingUser.setNik(updatedUser.getNik());
	            
	            session.update(existingUser);
	            tx.commit();
	            return existingUser;
	        }
	        return null;
		}catch (Exception e) {
	        if (tx != null) {
	            tx.rollback();
	        }
	        throw new DataAccessResourceFailureException("Error updating user", e);
	    }
	}

	@Override
	public Boolean deleteUser(Long id) {
	    Transaction tx = null;
	    try (Session session = sessionFactory.openSession()) {
	        tx = session.beginTransaction();
	        
	        User user = session.get(User.class, id);
	        if (user != null) {
	            session.delete(user);
	            tx.commit();
	            return true;
	        }
	        return false;
	    } catch (Exception e) {
	        if (tx != null) {
	            tx.rollback();
	        }
	        throw new DataAccessResourceFailureException("Error deleting user", e);
	    }
	}
}
