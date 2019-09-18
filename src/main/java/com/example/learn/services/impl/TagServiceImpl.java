package com.example.learn.services.impl;

import com.example.learn.models.Tag;
import com.example.learn.repositories.TagRepository;
import com.example.learn.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class TagServiceImpl implements TagService {

    @Autowired
    TagRepository tagRepository;

    @Override
    public Tag createNewTag(String name) {
        Tag tag = new Tag(name);
        Tag createdTag = tagRepository.save(tag);
        return createdTag;
    }

    @Override
    public Tag updateTag(long tagId, String name) {
        Optional<Tag> findTag = tagRepository.findById(tagId);

        if (!findTag.isPresent()) {
            return null;
        }
        Tag updateTag = findTag.get();
        updateTag.setName(name);

        return tagRepository.save(updateTag);
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public void deleteTag(long tagId) {
        Optional<Tag> findTag = tagRepository.findById(tagId);
        if (findTag.isPresent()) {
            Tag deleteTag = findTag.get();
            tagRepository.delete(deleteTag);
        }
    }

    @Override
    public void createNewPostTag(long tagId, long postId) {
        Optional<Tag> findTag = tagRepository.findById(tagId);

    }
}
