package com.example.learn.services.impl;

import com.example.learn.daos.UserDAO;
import com.example.learn.models.Search;
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
    User createUser = userDAO.create(user);
    return createUser;
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
  public void removeAuthenticate(HttpServletRequest request) {
    HttpSession session = request.getSession(true);
    session.removeAttribute("loginSession");
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
    User currentUser = getCurrentUser(request);
    return currentUser == null ? 0 : currentUser.getId();
  }

  @Override
  public User getCurrentUser(HttpServletRequest request) {
    HttpSession session = request.getSession(true);
    User loginSession = (User) session.getAttribute("loginSession");
    if (loginSession != null) {
      return loginSession;
    }
    return null;
  }

  @Override
  public List<User> searchUserByNameOrEmail(String query) {
    return userDAO.searchUserByNameOrEmail(query);
  }

  //todo: UT
  @Override
  public Search<User> searchUserInOrderAndPagination(String query, String order, int page) {
    return userDAO.searchByPaginationAndOrderByName(query, order, page);
  }

  //todo: UT
  @Override
  public User updateUser(User user) {
    return userDAO.update(user);
  }

  //TODO: UT
  @Override
  public List<User> findAllUser() {
    return userDAO.findAll();
  }

  //TODO: UT
  @Override
  public long deleteUser(long userId) {
    return userDAO.delete(userId);
  }
}
