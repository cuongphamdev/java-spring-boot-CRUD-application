package com.example.learn.controllers;

import com.example.learn.models.Post;
import com.example.learn.models.User;
import com.example.learn.services.CommentService;
import com.example.learn.services.PostService;
import com.example.learn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserProfileController {

  private final int DEFAULT_PAGE = 1;

  @Autowired
  UserService userService;

  @Autowired
  CommentService commentService;

  @Autowired
  PostService postService;

  private ModelAndView getSummaryProfile(int userId, int pageNumber) {
    ModelAndView modelAndView = new ModelAndView("profile");
    long countPosts = postService.countPostByUserId(userId);
    long countComments = commentService.countCommentByUserId(userId);
    User userProfile = userService.findUserById(userId);
    List<Post> listPosts = postService.findAllPostByUserIdAndPagination(userId, pageNumber);
    modelAndView.addObject("countPosts", countPosts);
    modelAndView.addObject("countComments", countComments);
    modelAndView.addObject("userProfile", userProfile);
    modelAndView.addObject("listPosts", listPosts);
    return modelAndView;
  }

  @RequestMapping("/users/{userId}")
  public ModelAndView summaryProfileByPageDefault(@PathVariable(value = "userId") int userId) {
    return this.getSummaryProfile(userId, DEFAULT_PAGE);
  }


  @RequestMapping("/users/{userId}/pages/{page}")
  public ModelAndView summaryProfileByPage(@PathVariable(value = "userId") int userId, @PathVariable(value = "page") int page) {
    return this.getSummaryProfile(userId, page);
  }


}
