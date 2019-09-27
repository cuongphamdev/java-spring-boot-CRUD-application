package com.example.learn.daos;

import java.util.List;

public interface CrudDAO<T> {
  List<T> findAll();

  T findById(long id);

  T create(T t);

  T update(T t);

  long delete(long id);
}
