package com.example.learn.services.impl;

import com.example.learn.dtos.Comment;
import com.example.learn.dtos.Post;
import com.example.learn.dtos.User;
import com.example.learn.repositories.CommentRepository;
import com.example.learn.repositories.PostRepository;
import com.example.learn.repositories.UserRepository;
import com.example.learn.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

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
        Optional<User> userFindOptional =  userRepository.findById(userId);
        User user = userFindOptional.isPresent() ? userFindOptional.get() : null;

        Optional<Post> postFindOptional =  postRepository.findById(userId);
        Post post = postFindOptional.isPresent() ? postFindOptional.get() : null;

        Optional<Comment> commentFindOptional =  commentRepository.findById(parentId);
        Comment parent = commentFindOptional.isPresent() ? commentFindOptional.get() : null;

        Comment newComment = new Comment(content, user, post);

        if (parent != null) {
            newComment.setParent(parent);
        }

        Comment createdComment = commentRepository.save(newComment);
        return createdComment;
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
