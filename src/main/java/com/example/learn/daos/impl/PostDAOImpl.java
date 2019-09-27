package com.example.learn.daos.impl;

import com.example.learn.daos.PostDAO;
import com.example.learn.models.Post;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class PostDAOImpl extends CrudDAOImpl<Post> implements PostDAO {

  @PersistenceContext
  private EntityManager entityManager;

  public PostDAOImpl() {
    super("Post");
  }

  @Override
  public long countPostByUserId(long userId) {
    try {
      String hql = "SELECT COUNT (p) FROM Post p WHERE userId = :userId";
      Query query = entityManager.createQuery(hql);
      query.setParameter("userId", userId);
      return (long) query.getSingleResult();
    } catch (Exception e) {
      return 0;
    }
  }

  @Override
  public List<Post> findAllPostByUserId(long userId) {
    try {
      String hql = "FROM Post p WHERE userId = :userId";
      Query query = entityManager.createQuery(hql);
      query.setParameter("userId", userId);
      return query.getResultList();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public List<Post> findAllPostByUserIdAndPagination(int userId, int page) {
    try {
      String hql = "FROM Post p WHERE userId = :userId";
      Query query = entityManager.createQuery(hql);
      query.setParameter("userId", (long) userId);
      query.setFirstResult((page - 1) * 1);
      query.setMaxResults(5);
      return query.getResultList();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public List<Post> findAllPostPagination(int page) {
    try {
      String hql = "FROM Post ORDER BY id DESC";
      Query query = entityManager.createQuery(hql);
      query.setFirstResult((page - 1) * 1);
      query.setMaxResults(5);
      return query.getResultList();
    } catch (Exception e) {
      return null;
    }
  }
}
