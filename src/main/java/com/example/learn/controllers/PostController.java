package com.example.learn.controllers;

import com.example.learn.dtos.Post;
import com.example.learn.services.PostService;
import com.example.learn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public long deletePostById (@PathVariable(value = "id") long postId) {
        postService.deletePost(postId);
        return postId;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Post updatePost (@PathVariable(value = "id") long postId, @ModelAttribute Post post) {
        String title = post.getTitle();
        String content = post.getContent();
        Post postUpdated = postService.updatePost(postId, title, content);
        return postUpdated;
    }

    @RequestMapping(value = "/{id}")
    public ModelAndView getPostById (@PathVariable(value = "id") long postId) {
        return new ModelAndView("post", "post", postService.findPostById(postId));
    }

    @RequestMapping( method = RequestMethod.POST)
    public ModelAndView createPost (@ModelAttribute Post post, HttpServletRequest request) {
        String title = post.getTitle();
        String content = post.getContent();
        long userId = userService.getCurrentUserId(request);
        postService.createNewPost(title, content, userId);
        return new ModelAndView("redirect:/");
    }
 }
