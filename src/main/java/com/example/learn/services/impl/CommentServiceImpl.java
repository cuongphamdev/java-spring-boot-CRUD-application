package com.example.learn.services.impl;

import com.example.learn.daos.CommentDAO;
import com.example.learn.models.Comment;
import com.example.learn.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

  @Autowired
  private CommentDAO commentDAO;

  @Override
  public List<Comment> findAllRootCommentByPostId(long postId) {
    List<Comment> listComments = commentDAO.listRootCommentByPostId(postId);
    return listComments;

  }

  @Override
  public Comment findCommentById(long commentId) {
    return commentDAO.findById(commentId);
  }

  @Override
  public Comment createComment(String content, long postId, long userId, long parentId) {
    Comment comment = new Comment(content, postId, userId);
    if (parentId != 0) {
      comment.setParentId(parentId);
    }

    long commentId = commentDAO.create(comment);
    return commentDAO.findById(commentId);
  }

  @Override
  public Comment updateComment(long commentId, String content) {
    Comment comment = commentDAO.findById(commentId);
    if (comment == null) {
      return null;
    }
    comment.setContent(content);
    long updateCommentId = commentDAO.update(comment);
    return comment;
  }

  @Override
  public void deleteComment(long commentId) {
    long deletedCommentId = commentDAO.delete(commentId);
  }
}
