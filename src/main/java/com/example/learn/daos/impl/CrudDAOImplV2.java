package com.example.learn.daos.impl;

import com.example.learn.daos.CrudDAO;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class CrudDAOImplV2<T> implements CrudDAO<T> {

  @PersistenceContext
  private EntityManager entityManager;

  private String tableName;

  public CrudDAOImplV2(String tableName) {
    this.tableName = tableName;
  }

  @Override
  public List<T> findAll() {
    try {
      String hql = "FROM " + tableName + " ORDER BY id DESC";
      return entityManager.createQuery(hql).getResultList();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public T findById(long id) {
    try {
      String hql = "FROM " + tableName + " WHERE id = :id";
      Query query = entityManager.createQuery(hql)
              .setParameter("id", id);
      return (T) query.getSingleResult();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  @Transactional
  public T create(T t) {
    try {
      Session session = entityManager.unwrap(Session.class);
      session.save(t);
      return t;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  @Transactional
  public T update(T t) {
    try {
      Session session = entityManager.unwrap(Session.class);
      session.update(t);
      return t;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  @Transactional
  public long delete(long id) {
    try {
      T deleteItem = this.findById(id);
      entityManager.remove(deleteItem);
      return id;
    } catch (Exception e) {
      return 0;
    }
  }
}
