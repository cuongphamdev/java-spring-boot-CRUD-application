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

//  @Autowired
//  private SessionFactory sessionFactory;

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
    List<User> users = entityManager.createQuery(hql).getResultList();
    return users;
  }

  @Override
  public User findById(long id) {
    try {
      String hql = "FROM User WHERE id = :userId";
      Query query = entityManager.createQuery(hql)
              .setParameter("userId", id);
      User user = (User) query.getSingleResult();
      return user;
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
      String hql = "UPDATE User SET name = :name, password = :password " +
              "WHERE id = :userId";
      Query query = entityManager.createQuery(hql);
      query.setParameter("name", user.getName())
              .setParameter("password", user.getPassword())
              .setParameter("userId", user.getId());
      return query.executeUpdate();
    } catch (Exception e) {
      return 0;
    }
  }

  @Override
  @Transactional
  public long delete(long userId) {
    try {
      String hql = "DELETE FROM User WHERE id = :userId";
      Query query = entityManager.createQuery(hql).setParameter("userId", userId);
      return query.executeUpdate();
    } catch (Exception e) {
      System.out.println("Error: " + e.toString());
      return 0;
    }
  }
}
