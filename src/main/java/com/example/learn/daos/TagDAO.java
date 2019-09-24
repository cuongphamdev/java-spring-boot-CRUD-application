package com.example.learn.daos;

import com.example.learn.models.Tag;

import java.util.List;

public interface TagDAO extends CrudDAO<Tag> {
  List<Tag> searchTag(String queryString);
}
