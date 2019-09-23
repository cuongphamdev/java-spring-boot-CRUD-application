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
  public Comment updateComment(@PathVariable(value = "commentId") long commentId, @ModelAttribute Comment commentUpdate) {
    String content = commentUpdate.getContent();
    Comment commentUpdated = commentService.updateComment(commentId, content);
    return commentUpdated;
  }

  @RequestMapping(value = "/{postId}/comments", method = RequestMethod.POST)
  public ModelAndView createComment(@PathVariable(value = "postId") long postId, @ModelAttribute Comment newComment, HttpServletRequest request) {
    String content = newComment.getContent();
    long parentId = newComment.getParentId();
    long userId = userService.getCurrentUserId(request);
    commentService.createComment(content, postId, userId, parentId);
    return new ModelAndView("redirect:/posts/" + postId);
  }
}
