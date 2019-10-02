package com.example.learn.controllers;

import com.example.learn.models.Comment;
import com.example.learn.models.Post;
import com.example.learn.models.Search;
import com.example.learn.models.User;
import com.example.learn.services.CommentService;
import com.example.learn.services.PostService;
import com.example.learn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserProfileController {

  private final int DEFAULT_PAGE_NUMBER = 1;

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
  public ModelAndView searchPostByTitleAndContent(
          @PathVariable(value = "userId") int userId,
          @RequestParam(value = "q", required = false) String queryParam,
          @RequestParam(value = "page", required = false) Integer page,
          @RequestParam(value = "sortBy", required = false) String order
  ) {
    queryParam = queryParam != null ? queryParam : "";
    page = page != null ? page : DEFAULT_PAGE_NUMBER;
    order = order != null ? order : "a2z";
    ModelAndView modelAndView = new ModelAndView("profile");
    Search<Post> searchResult = postService.findPostByTitleAndContentAndTagNameWithUserId(queryParam, userId, order, page);
    User currentUser = userService.findUserById(userId);
    List<Comment> comments = commentService.findCommentsByUserId(userId);
    modelAndView.addObject("comments", comments);
    modelAndView.addObject("user", currentUser);
    modelAndView.addObject("searchPostData", searchResult);
    return modelAndView;
  }

}
