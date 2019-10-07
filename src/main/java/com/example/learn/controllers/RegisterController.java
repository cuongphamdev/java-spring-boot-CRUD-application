package com.example.learn.controllers;

import com.example.learn.models.User;
import com.example.learn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RegisterController {

  @Autowired
  private UserService userService;

  @RequestMapping("/register")
  public ModelAndView getRegisterForm() {
    return new ModelAndView("register");
  }

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public Object createNewUser(@ModelAttribute User user, HttpServletRequest request) {
    String name = user.getName().trim();
    String email = user.getEmail().trim();
    String password = user.getPassword().trim();
    long roleId = 1;
    try {
      User loginUser = userService.createNewUser(name, email, password, roleId);
      userService.setAuthenticate(request, loginUser);
    } catch (Exception e) {
      System.out.println("Error: " + e.toString());
      String message = "";
      if (userService.findUserByEmail(email) != null) {
        message = "The email is existed! Please try with other email.";
      }
      ModelAndView modelAndView = new ModelAndView("/register");
      modelAndView.addObject("message", message);
      modelAndView.addObject("email", email);
      modelAndView.addObject("name", name);
      return modelAndView;
    }

    return new ModelAndView("redirect:/");
  }
}
