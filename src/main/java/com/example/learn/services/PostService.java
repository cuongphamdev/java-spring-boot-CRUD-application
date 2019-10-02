package com.example.learn.services;

import com.example.learn.models.Post;
import com.example.learn.models.Search;

import java.util.List;

public interface PostService {
  public List<Post> findAllPosts();

  public Post findPostById(long postId);

  public Post createNewPost(String title, String content, long userId);

  public Post updatePost(long postId, String title, String content);

  public long deletePost(long postId);

  public long countPostByUserId(long userId);

  public List<Post> findAllPostByUserIdAndPagination(int userId, int page);

  public List<Post> findAllPostPagination(int page);

  public Post createPost(Post post);

  public Search<Post> findPostByTitleAndContentAndTagNameWithUserId(String query, long userId, String sort, int page);
}
