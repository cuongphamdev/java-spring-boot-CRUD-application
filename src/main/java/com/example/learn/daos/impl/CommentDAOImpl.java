package com.example.learn.daos.impl;

import com.example.learn.daos.CommentDAO;
import com.example.learn.models.Comment;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class CommentDAOImpl implements CommentDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Comment> findAll() {
    String hql = "FROM Comment";
    List<Comment> comments = entityManager.createQuery(hql).getResultList();
    return comments;
  }

  @Override
  public long create(Comment comment) {
    try {
      Session session = entityManager.unwrap(Session.class);
      return (long) session.save(comment);
    } catch (Exception e) {
      return 0;
    }
  }

  @Override
  @Transactional
  public long update(Comment comment) {
    try {
      String hql = "UPDATE Comment SET content = :content " +
              "WHERE id = :comment_id";
      Query query = entityManager.createQuery(hql);
      query.setParameter("comment_id", comment.getId());
      query.executeUpdate();
      return comment.getId();
    } catch (Exception e) {
      return 0;
    }
  }

  @Override
  @Transactional
  public long delete(Comment comment) {
    try {
      entityManager.remove(comment);
      return comment.getId();
    } catch (Exception e) {
      return 0;
    }
  }


  @Override
  public List<Comment> listRootCommentByPostId(long postId) {
    try {
      String hql = "FROM Comment WHERE parentId = null AND postId = :postId";
      List<Comment> comments = entityManager.createQuery(hql)
              .setParameter("postId", postId).getResultList();
      return comments;
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public Comment findById(long commentId) {
    try {
      String hql = "FROM Comment WHERE id = :commentId";
      Query query = entityManager.createQuery(hql)
              .setParameter("commentId", commentId);
      return (Comment) query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public long countCommentByUserId(long userId) {
    try {
      String hql = "SELECT COUNT(c) FROM Comment c WHERE c.userId = :userId";
      Query query = entityManager.createQuery(hql)
              .setParameter("userId", userId);
      return (long) query.getSingleResult();
    } catch (Exception e) {
      return 0;
    }
  }
}
