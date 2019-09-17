package com.example.learn.services;

import com.example.learn.dtos.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CommentService {
    public List<Comment> findAllComments();
    public List<Comment> findAllCommentsByPostId (long postId);
    public List<Comment> findAllCommentByUserId (long userId);
    public List<Comment> findAllRootCommentByPostId(long postId);
    public Comment createComment (String content, long postId, long userId, long parentId);
    public Comment updateComment (long commentId, String content);
    public void deleteComment (long commentId);
}
