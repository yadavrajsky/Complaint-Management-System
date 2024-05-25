package com.example.service;

import java.util.List;
import java.util.UUID;

import com.example.model.UserRole;
import com.example.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

public class UserRoleService {

    public void assignRole(UserRole userRole) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(userRole);
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

    public UserRole findUserRoleByUserIdAndRoleId(UUID userId, UUID roleId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT ur FROM UserRole ur WHERE ur.user.id = :userId AND ur.role.id = :roleId";
            TypedQuery<UserRole> query = entityManager.createQuery(jpql, UserRole.class);
            query.setParameter("userId", userId);
            query.setParameter("roleId", roleId);
            return query.getSingleResult();
        } catch (Exception e) {
            // Handle any exceptions here (e.g., no result found)
            // e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }
    }

    public List<UserRole> findAllUserRoles() {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT ur FROM UserRole ur";
            TypedQuery<UserRole> query = entityManager.createQuery(jpql, UserRole.class);
            return query.getResultList();
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }
    }

  public UserRole findUserRoleByUserId(UUID userId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT ur FROM UserRole ur WHERE ur.user.id = :userId";
            TypedQuery<UserRole> query = entityManager.createQuery(jpql, UserRole.class);
            query.setParameter("userId", userId);
            return query.getSingleResult();
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }
 
    }  
    public void deleteUserRole(UserRole userRole) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(entityManager.merge(userRole));
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

    public void updateUserRole(UserRole userRole) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(userRole);
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
