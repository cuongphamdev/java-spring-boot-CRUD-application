package com.example.learn.daos;

import com.example.learn.models.Post;

import java.util.List;

public interface PostDAO extends CrudDAO<Post> {
  public long countPostByUserId(long userId);

  public List<Post> findAllPostByUserId(long userId);

  public List<Post> findAllPostByUserIdAndPagination(int userId, int page);

  public List<Post> findAllPostPagination(int page);
}
