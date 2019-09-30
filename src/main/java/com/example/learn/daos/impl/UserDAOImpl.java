package com.example.learn.daos.impl;

import com.example.learn.daos.UserDAO;
import com.example.learn.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
    queryString = "%" + queryString + "%";
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<User> criteria = builder.createQuery(User.class);
    Root<User> root = criteria.from(User.class);
    criteria.select(root);
    criteria.where(builder.or(builder.like(root.get("name"), queryString), builder.like(root.get("email"), queryString)));
    return entityManager.createQuery(criteria).setMaxResults(5).getResultList();
  }
}
