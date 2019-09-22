package com.example.learn.daos;

import com.example.learn.models.Comment;

import java.util.List;

public interface CommentDAO extends CrudDAO<Comment>{
  public List<Comment> listRootCommentByPostId(long postId);
}
