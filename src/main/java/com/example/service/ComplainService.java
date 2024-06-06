package com.example.service;

import java.util.List;
import java.util.UUID;

import com.example.model.Complain;
import com.example.model.User;
import com.example.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

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
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public Complain findComplainById(UUID id) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return entityManager.find(Complain.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }
    }

    public List<Complain> findAllComplains(String searchQuery) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Complain> query = null;
            if (searchQuery != null && !searchQuery.isEmpty()) {
                String jpql = "SELECT c FROM Complain c WHERE c.complainType LIKE :searchQuery OR c.complainDescription LIKE :searchQuery";
                query = entityManager.createQuery(jpql, Complain.class);
                query.setParameter("searchQuery", "%" + searchQuery + "%");
            }
            else 
            {
                String jpql = "SELECT c FROM Complain c";
                query = entityManager.createQuery(jpql, Complain.class);
            }
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public List<Complain> findComplainsByUserId(UUID userId, String searchQuery) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Complain> query = null;
            if (searchQuery != null && !searchQuery.isEmpty()) {
                String jpql = "SELECT c FROM Complain c WHERE c.createdByUser.id = :userId AND (c.complainType LIKE :searchQuery OR c.complainDescription LIKE :searchQuery)";
                query = entityManager.createQuery(jpql, Complain.class);
                query.setParameter("searchQuery", "%" + searchQuery + "%");
            }
            else 
            {
                String jpql = "SELECT c FROM Complain c WHERE c.createdByUser.id = :userId";
                query = entityManager.createQuery(jpql, Complain.class);

            }
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }
    }

    public List<Complain> findComplainsCreatedByUserId(UUID userId, String searchQuery) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Complain> query = null;
            if (searchQuery != null && !searchQuery.isEmpty()) {
                String jpql = "SELECT c FROM Complain c WHERE c.createdForUser.id = :userId AND (c.complainType LIKE :searchQuery OR c.complainDescription LIKE :searchQuery)";
                query = entityManager.createQuery(jpql, Complain.class);
                query.setParameter("searchQuery", "%" + searchQuery + "%");
            } else {
                String jpql = "SELECT c FROM Complain c WHERE c.createdForUser.id = :userId";
                query = entityManager.createQuery(jpql, Complain.class);
            }
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }
    }

    public List<Complain> findComplainsAssignedToUserId(UUID userId, String searchQuery) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Complain> query = null;
            if (searchQuery != null && !searchQuery.isEmpty()) {
                String jpql = "SELECT c FROM Complain c WHERE c.assignedTo.id = :userId AND (c.complainType LIKE :searchQuery OR c.complainDescription LIKE :searchQuery)";
                query = entityManager.createQuery(jpql, Complain.class);
                query.setParameter("searchQuery", "%" + searchQuery + "%");
            } else {
                String jpql = "SELECT c FROM Complain c WHERE c.assignedTo.id = :userId";
                query = entityManager.createQuery(jpql, Complain.class);
            }
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }
}
