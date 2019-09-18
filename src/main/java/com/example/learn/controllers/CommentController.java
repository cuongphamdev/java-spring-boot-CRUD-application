package com.example.learn.controllers;

import com.example.learn.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class CommentController {

    @Autowired
    CommentService commentService;

//    public Comment createComment (String content, long postId, long userId

}
