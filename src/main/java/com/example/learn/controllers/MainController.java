package com.example.learn.controllers;

import com.example.learn.dtos.Post;
import com.example.learn.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    PostService postService;

    @RequestMapping(value = "/")
    public ModelAndView home (HttpServletRequest request) {
        List<Post> posts = postService.findAllPosts();
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("posts", posts);
        return modelAndView;
    }
}
