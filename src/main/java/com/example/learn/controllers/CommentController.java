package com.example.learn.controllers;

import com.example.learn.models.Comment;
import com.example.learn.services.CommentService;
import com.example.learn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/posts")
public class CommentController {

  @Autowired
  private CommentService commentService;

  @Autowired
  private UserService userService;

  @RequestMapping(value = "/{postId}/comments/{commentId}", method = RequestMethod.PUT)
  public Comment updateComment(@PathVariable(value = "commentId") long commentId, @ModelAttribute Comment commentUpdate, HttpServletRequest request) {
    long currentUserId = userService.getCurrentUserId(request);
    Comment foundComment = commentService.findCommentById(commentId);
    if (foundComment.getUserId() != currentUserId) {
      return null;
    }
    String content = commentUpdate.getContent();
    return commentService.updateComment(commentId, content);
  }

  @RequestMapping(value = "/{postId}/comments/{commentId}", method = RequestMethod.DELETE)
  public long deleteComment(@PathVariable(value = "commentId") long commentId, HttpServletRequest request) {
    long currentUserId = userService.getCurrentUserId(request);
    Comment foundComment = commentService.findCommentById(commentId);
    if (foundComment.getUserId() != currentUserId) {
      return 0;
    }
    commentService.deleteComment(commentId);
    return commentId;
  }

  @RequestMapping(value = "/{postId}/comments", method = RequestMethod.POST)
  public ModelAndView createComment(@PathVariable(value = "postId") long postId, @ModelAttribute Comment newComment, HttpServletRequest request) {
    String content = newComment.getContent();
    long parentId = newComment.getParentId();
    long userId = userService.getCurrentUserId(request);
    if (userId != 0) {
      commentService.createComment(content, postId, userId, parentId);
    }
    return new ModelAndView("redirect:/posts/" + postId);
  }
}
