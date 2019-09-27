package com.example.learn.controllers;

import com.example.learn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LogoutController {

  @Autowired
  UserService userService;

  @RequestMapping("/logout")
  public ModelAndView logout(HttpServletRequest request) {
    userService.removeAuthenticate(request);
    return new ModelAndView("redirect:/login");
  }
}
