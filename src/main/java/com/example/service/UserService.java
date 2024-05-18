package com.example.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import com.example.model.User;
import com.example.model.UserRole;
import com.example.util.JPAUtil;


public class UserService {
    public void registerUser(User user) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            // e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }
    public User findUserByEmail(String email) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            // Create a JPQL query to find the user by email
            String jpql = "SELECT u FROM User u WHERE u.email = :email";
            TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
            query.setParameter("email", email);
            // Execute the query and retrieve the user
            return query.getSingleResult();
        } catch (Exception e) {
            // e.printStackTrace();
            return null; // Return null if user is not found or an error occurs
        } finally {
            entityManager.close();
        }
    }

    public List<UserRole> findUserRoles(User user) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            // Create a JPQL query to find the roles by user
            String jpql = "SELECT ur FROM UserRole ur WHERE ur.user = :user";
            TypedQuery<UserRole> query = entityManager.createQuery(jpql, UserRole.class);
            query.setParameter("user", user);
            // Execute the query and retrieve the roles
            return query.getResultList();
        } catch (Exception e) {
            // e.printStackTrace();
            return null; // Return null if roles are not found or an error occurs
        } finally {
            entityManager.close();
        }
    }
	public List<User> findAllUsers() {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT ur FROM User ur";
            TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
            return query.getResultList();
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }

	}

    public User findUserById(Long userId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            // Find the user by their ID
            return entityManager.find(User.class, userId);
        } catch (Exception e) {
            // Handle exceptions or logging here if needed
            // e.printStackTrace();
            return null; // Return null if user is not found or an error occurs
        } finally {
            entityManager.close();
        }
    }
}
