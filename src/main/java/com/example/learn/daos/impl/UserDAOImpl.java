package com.example.learn.daos.impl;

import com.example.learn.daos.UserDAO;
import com.example.learn.models.User;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public User findByEmailAndPassword(String email, String password) {
    try {
      String hql = "FROM User WHERE email = :email AND password = :password";
      Query query = entityManager.createQuery(hql)
              .setParameter("email", email)
              .setParameter("password", password);
      return (User) query.getSingleResult();
    } catch (Exception e) {
      System.out.println("Error: " + e.toString());
      return null;
    }
  }

  @Override
  public List<User> findAll() {
    String hql = "FROM User";
    return entityManager.createQuery(hql).getResultList();
  }

  @Override
  public User findById(long id) {
    try {
      String hql = "FROM User WHERE id = :userId";
      Query query = entityManager.createQuery(hql)
              .setParameter("userId", id);
      return (User) query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public long create(User user) {
    try {
      Session session = entityManager.unwrap(Session.class);
      return (long) session.save(user);
    } catch (Exception e) {
      return 0;
    }
  }

  @Override
  @Transactional
  public long update(User user) {
    try {
      Session session = entityManager.unwrap(Session.class);
      session.update(user);
      return user.getId();
    } catch (Exception e) {
      return 0;
    }
  }

  @Override
  @Transactional
  public long delete(User user) {
    try {
      entityManager.remove(user);
      return user.getId();
    } catch (Exception e) {
      System.out.println("Error: " + e.toString());
      return 0;
    }
  }

  @Override
  public List<User> searchUserByNameOrEmail(String queryString) {
    String hql = "FROM User WHERE email = :queryString OR name LIKE :queryString";
    Query query = entityManager.createQuery(hql).setParameter("queryString", "%" + queryString + "%");
    List<User> result = query.getResultList();
    return result;
  }
}
