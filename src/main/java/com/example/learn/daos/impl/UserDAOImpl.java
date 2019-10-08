package com.example.learn.daos.impl;

import com.example.learn.daos.UserDAO;
import com.example.learn.models.Search;
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
      String hql = "FROM User WHERE lower(email)  = :email AND password = :password";
      Query query = entityManager.createQuery(hql)
              .setParameter("email", email.toLowerCase())
              .setParameter("password", password);
      return (User) query.getSingleResult();
    } catch (Exception e) {
      System.out.println("Error: " + e.toString());
      return null;
    }
  }

  @Override
  public List<User> searchUserByNameOrEmail(String queryString) {
    queryString = "%" + queryString.toLowerCase() + "%";
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<User> criteria = builder.createQuery(User.class);
    Root<User> root = criteria.from(User.class);
    criteria.select(root);
    criteria.where(builder.or(builder.like(root.get("name"), queryString), builder.like(root.get("email"), queryString)));
    return entityManager.createQuery(criteria).setMaxResults(5).getResultList();
  }

  @Override
  public Search<User> searchByPaginationAndOrderByName(String query, String order, int page) {
    int countItems = 0;
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<User> criteria = builder.createQuery(User.class);
    Root<User> root = criteria.from(User.class);
    criteria.select(root);
    criteria.where(builder.or(builder.like(builder.lower(root.get("name")), "%" + query.toLowerCase() + "%"), builder.like(builder.lower(root.get("email")), "%" + query + "%")));
    if (order.equals("a2z")) {//todo: move to service
      criteria.orderBy(builder.asc(root.get("name")));
    } else if (order.equals("z2a")) {
      criteria.orderBy(builder.desc(root.get("name")));
    }
    countItems = entityManager.createQuery(criteria).getResultList().size();//todo: move to service
    List<User> usersList = entityManager.createQuery(criteria).setFirstResult((page - 1) * 5).setMaxResults(5).getResultList();//todo: move to service
    int maxPages = countItems / 5 + (countItems % 5 != 0 ? 1 : 0);//todo: move to service
    Search<User> result = new Search<User>(usersList, countItems, maxPages, query.toLowerCase(), page);//todo: move to service
    result.setSortBy(order);
    return result;
  }

  @Override
  public User searchUserEqualEmail(String email) {
    try {
      CriteriaBuilder builder = entityManager.getCriteriaBuilder();
      CriteriaQuery<User> criteria = builder.createQuery(User.class);
      Root<User> root = criteria.from(User.class);
      criteria.select(root);
      criteria.where(builder.equal(root.get("email"), email));
      User found = entityManager.createQuery(criteria).getSingleResult();
      return found;
    } catch (Exception e) {
      return null;
    }
  }
}
