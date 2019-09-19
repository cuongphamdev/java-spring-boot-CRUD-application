package com.example.learn.services.impl;

import com.example.learn.models.Comment;
import com.example.learn.models.Post;
import com.example.learn.models.User;
import com.example.learn.repositories.CommentRepository;
import com.example.learn.repositories.PostRepository;
import com.example.learn.repositories.UserRepository;
import com.example.learn.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Comment> findAllComments() {
        List<Comment> listComments = commentRepository.findAll();
        return listComments;
    }

    @Override
    public List<Comment> findAllCommentsByPostId(long postId) {
        List<Comment> listComments = commentRepository.findCommentsByPostId(postId);
        return listComments;
    }

    @Override
    public List<Comment> findAllCommentByUserId(long userId) {
        List<Comment> listComments = commentRepository.findCommentsByUserId(userId);
        return listComments;
    }

    @Override
    public List<Comment> findAllRootCommentByPostId(long postId) {
        List<Comment> listComments = commentRepository.findRootCommentsListByPostId(postId);
        return listComments;
    }

    @Override
    public Comment createComment(String content, long postId, long userId, long parentId) {
        Comment comment = new Comment(content, postId, userId, parentId);
        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(long commentId, String content) {
        Optional<Comment> commentOptional =  commentRepository.findById(commentId);
        Comment comment = commentOptional.isPresent() ? commentOptional.get() : null;
        if (comment == null) {
            return null;
        }

        comment.setContent(content);
        Comment updateComment = commentRepository.save(comment);
        return updateComment;
    }

    @Override
    public void deleteComment(long commentId) {
        Optional<Comment> commentOptional =  commentRepository.findById(commentId);
        Comment comment = commentOptional.isPresent() ? commentOptional.get() : null;
        if (comment != null) {
            commentRepository.delete(comment);
        }
    }
}
