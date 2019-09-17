package com.example.learn.services;

import com.example.learn.dtos.Post;

import java.util.List;

public interface PostService {
    public List<Post> findAllPosts();
    public Post findPostById(long postId);
    public Post createNewPost (String title, String content, long userId);
    public Post updatePost (long postId, String title, String content);
    public void deletePost (long postId);
}
