package com.example.learn.services;

import com.example.learn.models.Comment;

import java.util.List;

public interface CommentService {
  public List<Comment> findAllRootCommentByPostId(long postId);

  public Comment findCommentById(long commentId);

  public Comment createComment(String content, long postId, long userId, long parentId);

  public Comment updateComment(long commentId, String content);

  public long deleteComment(long commentId);

  public long countCommentByUserId(long userId);

  public List<Comment> findCommentsByUserId(long userId);

  public List<Comment> findAllComment();
}
