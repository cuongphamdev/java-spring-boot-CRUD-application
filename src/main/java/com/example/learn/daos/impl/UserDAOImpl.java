package com.example.learn.daos.impl;

import com.example.learn.daos.UserDAO;
import com.example.learn.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class UserDAOImpl extends CrudDAOImpl<User> implements UserDAO {

  @PersistenceContext
  private EntityManager entityManager;

  public UserDAOImpl() {
    super("User");
  }

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
  public List<User> searchUserByNameOrEmail(String queryString) {
    String hql = "FROM User WHERE email LIKE :queryString OR name LIKE :queryString";
    Query query = entityManager.createQuery(hql).setParameter("queryString", "%" + queryString + "%");
    List<User> result = query.getResultList();
    return result;
  }
}
