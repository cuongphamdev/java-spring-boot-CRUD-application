package com.example.learn.services;

import com.example.learn.daos.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    public void createNewUser(String name, String email, String password);
    public User loginByEmailAndPassword (String email, String password);
    public boolean checkAuthentication (HttpServletRequest request);
    public void setAuthenticate (HttpServletRequest request, String email);
}
