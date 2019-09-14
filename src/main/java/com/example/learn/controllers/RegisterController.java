package com.example.learn.controllers;

import com.example.learn.daos.User;
import com.example.learn.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class RegisterController {

    @Autowired
    UserRepository userRepositor;

    @RequestMapping("/register")
    public ModelAndView getRegisterForm () {
        return new ModelAndView("register")
    }

    @RequestMapping("/register")
    public Object createNewUser (@RequestBody User user, HttpServletRequest request) {
        String name = user.getName();
        String email = user.getEmail();
        String password = user.getPassword();
        try{
            userRepositor.createNewUser(name, email, password);
            HttpSession session = request.getSession(true);
            session.setAttribute("loginSession", email);
        } catch (Exception e) {
            return new ModelAndView("register");
        }


    }
}
