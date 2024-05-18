package com.example.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import com.example.model.Role;
import com.example.util.JPAUtil;

public class RoleService {

    public void registerRole(Role role) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(role);
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

    public Role findRoleById(Long id) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return entityManager.find(Role.class, id);
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }
    }

    public List<Role> findAllRoles() {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT  r FROM Role r";
            TypedQuery<Role> query = entityManager.createQuery(jpql, Role.class);
            return query.getResultList();
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }
    }

    public void deleteRole(Role role) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(entityManager.merge(role));
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

    public void updateRole(Role role) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(role);
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
