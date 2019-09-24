package com.example.learn.daos.impl;

import com.example.learn.daos.PostDAO;
import com.example.learn.models.Post;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class PostDAOImpl implements PostDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Post> findAll() {
    String hql = "FROM Post";
    List<Post> posts = entityManager.createQuery(hql).getResultList();
    return posts;
  }

  @Override
  public Post findById(long id) {
    try {
      String hql = "FROM Post WHERE id = :postId";
      Query query = entityManager.createQuery(hql)
              .setParameter("postId", id);
      return (Post) query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public long create(Post post) {
    try {
      Session session = entityManager.unwrap(Session.class);
      long postId = (long) session.save(post);
      return postId;
    } catch (Exception e) {
      return 0;
    }
  }

  @Override
  @Transactional
  public long update(Post post) {
    try {
      Session session = entityManager.unwrap(Session.class);
      session.update(post);
      return post.getId();
    } catch (Exception e) {
      return 0;
    }
  }

  @Override
  @Transactional
  public long delete(Post post) {
    try {
      entityManager.remove(post);
      return post.getId();
    } catch (Exception e) {
      return 0;
    }
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
      query.setMaxResults(1);
      return query.getResultList();
    } catch (Exception e) {
      return null;
    }
  }
}
