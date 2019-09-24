package com.example.learn.controllers;

import com.example.learn.models.Post;
import com.example.learn.models.Tag;
import com.example.learn.services.PostService;
import com.example.learn.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class MainController {

  @Autowired
  private PostService postService;

  @Autowired
  private TagService tagService;

  @RequestMapping(value = "/")
  public ModelAndView home(HttpServletRequest request) {
    List<Post> posts = postService.findAllPosts();
    List<Tag> tags = tagService.getAllTags();
    ModelAndView modelAndView = new ModelAndView("index");
    modelAndView.addObject("posts", posts);
    modelAndView.addObject("tags", tags);
    return modelAndView;
  }
}
