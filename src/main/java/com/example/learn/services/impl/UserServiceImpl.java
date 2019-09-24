package com.example.learn.services.impl;

import com.example.learn.daos.UserDAO;
import com.example.learn.models.User;
import com.example.learn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserDAO userDAO;

  @Override
  public User createNewUser(String name, String email, String password, long roleId) {
    User user = new User();
    user.setEmail(email);
    user.setName(name);
    user.setPassword(password);
    user.setRoleId(roleId);
    long loginUserId = userDAO.create(user);
    return userDAO.findById(loginUserId);
  }

  @Override
  public boolean checkAuthentication(HttpServletRequest request) {
    HttpSession session = request.getSession(true);
    Object loginSession = session.getAttribute("loginSession");
    return loginSession != null;
  }

  @Override
  public void setAuthenticate(HttpServletRequest request, User user) {
    HttpSession session = request.getSession(true);
    session.setAttribute("loginSession", user);
  }

  @Override
  public User findUserById(long userId) {
    return userDAO.findById(userId);
  }

  @Override
  public User loginByEmailAndPassword(String email, String password) {
    return userDAO.findByEmailAndPassword(email, password);
  }

  @Override
  public long getCurrentUserId(HttpServletRequest request) {
    HttpSession session = request.getSession(true);
    User loginSession = (User) session.getAttribute("loginSession");
    if (loginSession != null) {
      return loginSession.getId();
    }
    return 0;
  }

  @Override
  public List<User> searchUserByNameOrEmail(String query) {
    return userDAO.searchUserByNameOrEmail(query);
  }
}
