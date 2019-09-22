package com.example.learn.services.impl;

import com.example.learn.daos.CommentDAO;
import com.example.learn.daos.PostDAO;
import com.example.learn.models.Post;
import com.example.learn.models.User;
import com.example.learn.repositories.CommentRepository;
import com.example.learn.repositories.PostRepository;
import com.example.learn.repositories.UserRepository;
import com.example.learn.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Optional<User> authorOptional = userRepository.findById(userId);

        if (authorOptional.isPresent()) {
            Post post = new Post(title, content, authorOptional.get());
            long postCreatedId= postDAO.create(post);
            return postDAO.findById(postCreatedId);
        }
        return null;
    }

    @Override
    public Post updatePost(long postId, String title, String content) {
        Optional<Post> postOptional= postRepository.findById(postId);

        if (postOptional.isPresent()) {
            Post postUpdate = postOptional.get();
            postUpdate.setTitle(title);
            postUpdate.setContent(content);
            long updatedPostId = postDAO.create(postUpdate);
            return postDAO.findById(updatedPostId);
        }
        return null;
    }

    @Override
    public void deletePost(long postId) {
        postDAO.delete(postId);
    }
}
