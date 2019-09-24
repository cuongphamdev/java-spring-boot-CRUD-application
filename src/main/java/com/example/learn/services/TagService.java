package com.example.learn.services;

import com.example.learn.models.Tag;

import java.util.List;

public interface TagService {
  public Tag createNewTag(String name);

  public Tag updateTag(long tagId, String name);

  public Tag updateTagPost(Tag tag);

  public List<Tag> getAllTags();


  public Tag getTagById(long tagId);

  public long deleteTag(long tagId);

  public List<Tag> searchTags(String queryString);
}
