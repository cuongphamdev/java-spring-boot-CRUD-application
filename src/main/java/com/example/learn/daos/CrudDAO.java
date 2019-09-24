package com.example.learn.daos;

import java.util.List;

public interface CrudDAO<T> {
  List<T> findAll();
  T findById(long id);
  long create(T t);
  long update(T t);
  long delete(T t);
}
