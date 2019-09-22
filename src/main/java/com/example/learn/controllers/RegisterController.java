package com.example.learn.controllers;

import com.example.learn.daos.UserDAO;
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
    public ModelAndView getRegisterForm () {
        return new ModelAndView("register");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Object createNewUser (@ModelAttribute User user, HttpServletRequest request) {
        String name = user.getName();
        String email = user.getEmail();
        String password = user.getPassword();
        try{
            User loginUser = userService.createNewUser(name, email, password);
            userService.setAuthenticate(request, loginUser);
        } catch (Exception e) {
            String message = "";
            ModelAndView modelAndView = new ModelAndView("/register");
            modelAndView.addObject("message", message);
            modelAndView.addObject("email", email);
            modelAndView.addObject("name", name);
            return modelAndView;
        }

        return  new ModelAndView("redirect:/");
    }
}
