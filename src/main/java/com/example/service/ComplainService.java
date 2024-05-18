package com.example.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import com.example.model.Complain;
import com.example.model.User;
import com.example.util.JPAUtil;

public class ComplainService {

    public void createComplain(Complain complain) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(complain);
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

    public Complain findComplainById(Long id) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return entityManager.find(Complain.class, id);
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }
    }

    public List<Complain> findAllComplains() {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT c FROM Complain c";
            TypedQuery<Complain> query = entityManager.createQuery(jpql, Complain.class);
            return query.getResultList();
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }
    }

    public void updateComplain(Complain complain) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(complain);
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

    public void deleteComplain(Complain complain) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(entityManager.merge(complain));
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

    public List<Complain> findComplainsByUserId(Long userId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT c FROM Complain c WHERE c.createdByUser.id = :userId";
            TypedQuery<Complain> query = entityManager.createQuery(jpql, Complain.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }
    }

    public List<Complain> findComplainsCreatedByUserId(Long userId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT c FROM Complain c WHERE c.createdForUser.id = :userId";
            TypedQuery<Complain> query = entityManager.createQuery(jpql, Complain.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }
    }

    public List<Complain> findComplainsAssignedToUserId(Long userId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT c FROM Complain c WHERE c.assignedTo.id = :userId";
            TypedQuery<Complain> query = entityManager.createQuery(jpql, Complain.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }
    }

    public void assignTaskToUser(Complain complain, User user) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            complain.setAssignedTo(user);
            entityManager.merge(complain);
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
}
