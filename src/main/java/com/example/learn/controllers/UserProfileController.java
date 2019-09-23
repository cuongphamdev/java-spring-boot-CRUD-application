package com.example.learn.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserProfileController {

  @RequestMapping("/users/{userId}")
  public ModelAndView summaryProfile (@PathVariable(value = "userId") long userId) {
    ModelAndView modelAndView = new ModelAndView("user-profile");
      // TODO: get count number of the post and comments
      // TODO: create pagination for each page limit 10 post
    return modelAndView;
  }

}
