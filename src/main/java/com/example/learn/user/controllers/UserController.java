package com.example.learn.user.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    @RequestMapping(value = "/login")
    public String login (HttpServletRequest request) {
        return "login";
    }
}
