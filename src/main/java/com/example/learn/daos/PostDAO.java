package com.example.learn.daos;

import com.example.learn.models.Post;
import com.example.learn.models.Search;

import java.util.List;

public interface PostDAO extends CrudDAO<Post> {
  public long countPostByUserId(long userId);

  public List<Post> findAllPostByUserId(long userId);

  public List<Post> findAllPostByUserIdAndPagination(int userId, int page);

  public List<Post> findAllPostPagination(int page);

  public List<Post> findPostByTitleAndContent(String query);

  public List<Post> findPostByTitleAndContentAndTagName(String query);

  public Search<Post> searchPostByTitleAndContentAndTagNameWithUserId(String query, long userId, int pageId);
}
