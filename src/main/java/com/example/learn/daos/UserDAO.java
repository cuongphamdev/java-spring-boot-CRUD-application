package com.example.learn.daos;

import com.example.learn.models.User;

public interface UserDAO extends CrudDAO<User> {
  User findByEmailAndPassword(String email, String password);
}
