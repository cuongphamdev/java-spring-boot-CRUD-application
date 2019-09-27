package com.example.learn.controllers;

import com.example.learn.models.Post;
import com.example.learn.models.Tag;
import com.example.learn.services.PostService;
import com.example.learn.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

  private ModelAndView getPostAndTagModelView(int pageNumber) {
    ModelAndView modelAndView = new ModelAndView("index");
    List<Post> posts = postService.findAllPostPagination(pageNumber);
    List<Tag> tags = tagService.getAllTags();
    modelAndView.addObject("posts", posts);
    modelAndView.addObject("tags", tags);
    modelAndView.addObject("currentPage", pageNumber);
    return modelAndView;
  }


  @RequestMapping(value = "/")
  public ModelAndView home(HttpServletRequest request) {
    return this.getPostAndTagModelView(1);
  }

  @RequestMapping("/pages/{page}")
  public ModelAndView summaryProfileByPage(@PathVariable(value = "page") int page) {
    return this.getPostAndTagModelView(page);
  }
}
