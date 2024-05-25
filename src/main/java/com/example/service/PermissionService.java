package com.example.service;

import java.util.List;
import java.util.UUID;

import com.example.model.Permission;
import com.example.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

public class PermissionService {

    public void createPermission(Permission permission) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(permission);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            // Handle exceptions
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public Permission findPermissionByRoleId(UUID roleId) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT p FROM Permission p WHERE p.role.id = :roleId";
            TypedQuery<Permission> query = entityManager.createQuery(jpql, Permission.class);
            query.setParameter("roleId", roleId);
            return query.getSingleResult();
        } catch (Exception e) {
            // Handle exceptions (e.g., no result found)
            // e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }
    }

    public void updatePermission(Permission permission) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(permission);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            // Handle exceptions
            // e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public void deletePermission(Permission permission) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(entityManager.merge(permission));
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            // Handle exceptions
            // e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public List<Permission> findAllPermissions() {
         EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT ur FROM Permission ur";
            TypedQuery<Permission> query = entityManager.createQuery(jpql, Permission.class);
            return query.getResultList();
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }

	}
}
