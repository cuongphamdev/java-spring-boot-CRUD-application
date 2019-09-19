package com.example.learn.services.impl;

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
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    @Override
    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post findPostById(long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isPresent()) {
            return postOptional.get();
        }
        return null;
    }

    @Override
    public Post createNewPost(String title, String content, long userId) {
        Optional<User> authorOptional = userRepository.findById(userId);

        if (authorOptional.isPresent()) {
            Post post = new Post(title, content, authorOptional.get());
            Post postCreated = postRepository.save(post);
            return postCreated;
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

            Post updatePost = postRepository.save(postUpdate);
            return updatePost;
        }

        return null;
    }

    @Override
    public void deletePost(long postId) {
        Optional<Post> postOptional= postRepository.findById(postId);
        if (postOptional.isPresent()) {
            commentRepository.deleteCommentsByPostId(postId);
            postRepository.delete(postOptional.get());
        }
    }
}
