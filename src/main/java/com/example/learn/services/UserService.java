package com.example.learn.services;

import com.example.learn.models.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
  public User createNewUser(String name, String email, String password, long roleId);

  public User loginByEmailAndPassword(String email, String password);

  public boolean checkAuthentication(HttpServletRequest request);

  public void setAuthenticate(HttpServletRequest request, User user);

  public User findUserById(long userId);

  public long getCurrentUserId(HttpServletRequest request);
}
