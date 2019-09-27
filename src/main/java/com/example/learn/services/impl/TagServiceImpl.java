package com.example.learn.services.impl;

import com.example.learn.daos.TagDAO;
import com.example.learn.models.Tag;
import com.example.learn.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

  @Autowired
  TagDAO tagDAO;

  @Override
  public Tag createNewTag(String name) {
    Tag tag = new Tag();
    tag.setName(name);
    return tagDAO.create(tag);
  }

  @Override
  public Tag updateTag(long tagId, String name) {
    Tag tagFound = tagDAO.findById(tagId);
    if (tagFound != null) {
      tagFound.setName(name);
      return tagDAO.update(tagFound);
    }
    return null;
  }

  @Override
  public List<Tag> getAllTags() {
    return tagDAO.findAll();
  }

  @Override
  public long deleteTag(long tagId) {
    return tagDAO.delete(tagId);
  }

  @Override
  public List<Tag> searchTags(String queryString) {
    return tagDAO.searchTag(queryString);
  }

  @Override
  public Tag getTagById(long tagId) {
    return tagDAO.findById(tagId);
  }

  @Override
  public Tag updateTagPost(Tag tag) {
    return tagDAO.update(tag);
  }
}
