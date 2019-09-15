package com.example.learn.controllers;

import com.example.learn.daos.User;
import com.example.learn.repositories.UserRepository;
import com.example.learn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/login")
    public ModelAndView login (HttpServletRequest request) {
        if (userService.checkAuthentication(request)) {
            return new ModelAndView("redirect:/");
        }
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView doLogin (@ModelAttribute User user, HttpServletRequest request) {
        String email = user.getEmail();
        String password = user.getPassword();
        User loginUser = userService.loginByEmailAndPassword(email, password);
        if (loginUser == null) {
            String message = "The input information is incorrect";
            ModelAndView modelAndView = new ModelAndView("login");
            modelAndView.addObject("message", message);
            modelAndView.addObject("email", email);
            return modelAndView;
        }
        userService.setAuthenticate(request, email);
        return new ModelAndView("redirect:/");
    }
}
