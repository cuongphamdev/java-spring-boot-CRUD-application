package com.example.learn.services;

import com.example.learn.models.Tag;

import java.util.List;

public interface TagService {
    public Tag createNewTag (String name);
    public Tag updateTag (long tagId, String name);
    public List<Tag> getAllTags ();
    public void deleteTag (long tagId);
    public void createNewPostTag (long tagId, long postId);
}
