package com.example.learn.daos.impl;

import com.example.learn.daos.CrudDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class CrudDAOImpl<T> implements CrudDAO<T> {

  private String tableName;

  private final Log log = LogFactory.getLog(CrudDAO.class);

  @PersistenceContext
  private EntityManager entityManager;


  public CrudDAOImpl(String tableName) {
    this.tableName = tableName;
  }

  @Override
  public List<T> findAll() {
    java.lang.String hql = "FROM " + tableName;
    List<T> resultLists = entityManager.createQuery(hql).getResultList();
    return resultLists;
  }

  @Override
  public T findById(long id) {
    try {
      java.lang.String hql = "FROM " + tableName + " WHERE id = :id";
      Query query = entityManager.createQuery(hql)
              .setParameter("id", id);
      return (T) query.getSingleResult();
    } catch (Exception e) {
      log.warn("error: " + e.toString());
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
      log.warn("error: " + e.toString());
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
      log.warn("error: " + e.toString());
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
      log.warn("error: " + e.toString());
      return 0;
    }
  }
}
