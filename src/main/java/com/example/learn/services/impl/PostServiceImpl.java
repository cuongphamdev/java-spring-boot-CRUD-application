package com.example.learn.services.impl;

import com.example.learn.daos.PostDAO;
import com.example.learn.daos.UserDAO;
import com.example.learn.models.Post;
import com.example.learn.models.User;
import com.example.learn.repositories.CommentRepository;
import com.example.learn.repositories.PostRepository;
import com.example.learn.repositories.UserRepository;
import com.example.learn.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CommentRepository commentRepository;

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
      long postCreatedId = postDAO.create(post);
      return postDAO.findById(postCreatedId);
    }
    return null;
  }

  @Override
  public Post updatePost(long postId, String title, String content) {
    Post postFound = postDAO.findById(postId);

    if (postFound != null) {
      postFound.setTitle(title);
      postFound.setContent(content);
      long updatedPostId = postDAO.update(postFound);
      return postDAO.findById(updatedPostId);
    }
    return null;
  }

  @Override
  public long deletePost(long postId) {
    return postDAO.delete(postId);
  }
}
