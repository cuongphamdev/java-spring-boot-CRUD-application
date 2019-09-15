package com.example.learn.services.impl;

import com.example.learn.daos.User;
import com.example.learn.repositories.UserRepository;
import com.example.learn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public void createNewUser(String name, String email, String password) {
        userRepository.createNewUser(name, email, password);
    }

    @Override
    public boolean checkAuthentication(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        Object loginSession = session.getAttribute("loginSession");
        return loginSession != null;
    }

    @Override
    public void setAuthenticate(HttpServletRequest request, String email) {
        HttpSession session = request.getSession(true);
        session.setAttribute("loginSession", email);
    }

    @Override
    public User loginByEmailAndPassword(String email, String password) {
        User loginUser = userRepository.findUserByEmailAndPassword(email, password);
        return loginUser;
    }
}
