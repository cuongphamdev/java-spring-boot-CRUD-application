package com.example.learn.daos;

import com.example.learn.models.Search;
import com.example.learn.models.User;

import java.util.List;

public interface UserDAO extends CrudDAO<User> {
  User findByEmailAndPassword(String email, String password);

  List<User> searchUserByNameOrEmail(String queryString);
  Search<User> searchByPaginationAndOrderByName(String query, String order, int page);
}
