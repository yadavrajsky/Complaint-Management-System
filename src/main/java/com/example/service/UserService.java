package com.example.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.example.model.User;
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
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }
}
