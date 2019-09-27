package com.example.learn.daos.impl;

import com.example.learn.daos.CommentDAO;
import com.example.learn.models.Comment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class CommentDAOImpl extends CrudDAOImpl<Comment> implements CommentDAO {

  @PersistenceContext
  private EntityManager entityManager;

  public CommentDAOImpl() {
    super("Comment");
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
