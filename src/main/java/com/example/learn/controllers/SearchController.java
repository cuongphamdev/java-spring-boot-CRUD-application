package com.example.learn.controllers;

import com.example.learn.models.Post;
import com.example.learn.models.User;
import com.example.learn.services.PostService;
import com.example.learn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

  @Autowired
  UserService userService;

  @Autowired
  PostService postService;

  @RequestMapping("/user/{queryParam}")
  public List<User> searchUserByNameOrEmail(@PathVariable("queryParam") String queryParam) {
    return userService.searchUserByNameOrEmail(queryParam);
  }

  @RequestMapping("/post/{queryParam}")
  public List<Post> searchPostByTitleAndContent(@PathVariable("queryParam") String queryParam) {
    return postService.findPostByTitleAndContent(queryParam);
  }
}
