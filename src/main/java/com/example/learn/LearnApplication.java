package com.example.learn;

import com.example.learn.services.CommentService;
import com.example.learn.services.PostService;
import com.example.learn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LearnApplication implements CommandLineRunner {

	@Autowired
	UserService userService;

	@Autowired
	PostService postService;

	@Autowired
	CommentService commentService;

	public static void main(String[] args) {
		SpringApplication.run(LearnApplication.class, args);
	}

	@Override
	public void run(String... args) {
//		userService.createNewUser("Cuong", "cuongpham.dev@gmail.com", "123456");
//		userService.createNewUser("test", "test@gmail.com", "123456");
//		postService.createNewPost("Post title 1", "post content post content post content post content post content post content post content post content post content post content post content post content post content post content", 1);
//		postService.createNewPost("Post title 2", "post content post content post content post content post content post content post content post content post content post content post content post content post content post content", 2);
//		postService.createNewPost("Post title 3", "post content post content post content post content post content post content post content post content post content post content post content post content post content post content", 1);
//		postService.createNewPost("Post title 4", "post content post content post content post content post content post content post content post content post content post content post content post content post content post content", 2);
//		postService.createNewPost("Post title 5", "post content post content post content post content post content post content post content post content post content post content post content post content post content post content", 1);
//		postService.createNewPost("Post title 6", "post content post content post content post content post content post content post content post content post content post content post content post content post content post content", 2);
//		postService.createNewPost("Post title 7", "post content post content post content post content post content post content post content post content post content post content post content post content post content post content", 1);
//		postService.createNewPost("Post title 8", "post content post content post content post content post content post content post content post content post content post content post content post content post content post content", 2);
//		commentService.createComment("This is comment", 1, 1, 0);
	}
}
