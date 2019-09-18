package com.example.learn.controllers;

import com.example.learn.models.Comment;
import com.example.learn.models.Post;
import com.example.learn.services.CommentService;
import com.example.learn.services.PostService;
import com.example.learn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

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
        Post currentPost = postService.findPostById(postId);
        List<Comment> commentLists = commentService.findAllRootCommentByPostId(postId);
        ModelAndView modelAndView = new ModelAndView("post");

        modelAndView.addObject("post", currentPost);
        modelAndView.addObject("comments", commentLists);

        return modelAndView;
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
