package com.example.learn.daos.impl;

import com.example.learn.daos.CommentDAOV2;
import com.example.learn.models.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class CommentDAOImplV2 extends CrudDAOImpl<Comment> implements CommentDAOV2 {

  @PersistenceContext
  private EntityManager entityManager;

  public CommentDAOImplV2() {
    super("Comment");
  }

  @Override
  public List<Comment> listRootCommentByPostId(long postId) {
    String hql = "FROM Comment WHERE parentId = null AND postId = :postId ORDER BY id DESC";
    return entityManager.createQuery(hql)
            .setParameter("postId", postId).getResultList();
  }

  @Override
  public long countCommentByUserId(long userId) {
    String hql = "SELECT COUNT(c) FROM Comment c WHERE c.userId = :userId";
    Query query = entityManager.createQuery(hql)
            .setParameter("userId", userId);
    return (long) query.getSingleResult();
  }

  @Override
  public List<Comment> findCommentByUserId(long userId) {
    String hql = "FROM Comment WHERE userId = :userId";
    Query query = entityManager.createQuery(hql)
            .setParameter("userId", userId);
    return (List<Comment>) query.getResultList();
  }
}
