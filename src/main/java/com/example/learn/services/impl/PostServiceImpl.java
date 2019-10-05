package com.example.learn.services.impl;

import com.example.learn.daos.PostDAO;
import com.example.learn.daos.UserDAO;
import com.example.learn.models.Post;
import com.example.learn.models.Search;
import com.example.learn.models.User;
import com.example.learn.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

  @Autowired
  private PostDAO postDAO;

  @Autowired
  private UserDAO userDAO;

  @Override
  public List<Post> findAllPosts() {
    return postDAO.findAll();
  }

  @Override
  public Post findPostById(long postId) {
    return postDAO.findById(postId);
  }

  @Override
  public Post createNewPost(String title, String content, long userId) {
    User author = userDAO.findById(userId);
    if (author != null) {
      Post post = new Post(title, content, author.getId());
      return postDAO.create(post);
    }
    return null;
  }

  @Transactional
  @Override
  public Post updatePost(long postId, String title, String content) {
    Post postFound = postDAO.findById(postId);

    if (postFound != null) {
      postFound.setTitle(title);
      postFound.setContent(content);
      return postDAO.update(postFound);
    }
    return null;
  }

  @Transactional
  @Override
  public long deletePost(long postId) {
    return postDAO.delete(postId);
  }

  @Override
  public long countPostByUserId(long userId) {
    return postDAO.countPostByUserId(userId);
  }

  @Override
  public List<Post> findAllPostByUserIdAndPagination(int userId, int page) {
    return postDAO.findAllPostByUserIdAndPagination(userId, page);
  }

  @Override
  public Post createPost(Post post) {
    return postDAO.create(post);
  }

  @Override
  public List<Post> findAllPostPagination(int page) {
    return postDAO.findAllPostPagination(page);
  }

  //todo: UT
  @Override
  public Search<Post> findPostByTitleAndContentAndTagNameWithUserId(String query, long userId, String order, int page) {
    return postDAO.searchPostByTitleAndContentAndTagNameWithUserId(query, userId, order, page);
  }

  //todo: UT
  @Override
  public Search<Post> searchPostByTitleAndContentAndNameUserWithSortAndPageBreak(String postQuery, String order, int page, int pageBreak, int tagId) {
    return postDAO.searchPostByTitleAndContentAndNameUserWithSortAndPageBreak(postQuery, order, page, pageBreak, tagId);
  }
}
