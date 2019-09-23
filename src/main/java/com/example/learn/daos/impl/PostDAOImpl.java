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
      System.out.println("Error: " + e.toString());
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
      String hql = "UPDATE Post SET title = :title, content = :content " +
              "WHERE id = :postId";
      Query query = entityManager.createQuery(hql);
      query.setParameter("title", post.getTitle())
              .setParameter("content", post.getContent());
      return query.executeUpdate();
    } catch (Exception e) {
      return 0;
    }
  }

  @Override
  @Transactional
  public long delete(long postId) {
    try {
      String hql = "DELETE FROM Post p WHERE p.id = :postId";
      Query query = entityManager.createQuery(hql).setParameter("postId", postId);
      return query.executeUpdate();
    } catch (Exception e) {
      return 0;
    }
  }
}