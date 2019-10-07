package com.example.learn.services;

import com.example.learn.models.*;

public class ServiceDataTest {
  public static final User dummyUser = new User("Cuong", "cuongpham.dev@gmail.com", "e10adc3949ba59abbe56e057f20f883e");
  public static final User[] dummyUserList = {new User("Cuong", "cuongpham.dev@gmail.com", "e10adc3949ba59abbe56e057f20f883e"), new User("Cuong", "test1@gmail.com", "e10adc3949ba59abbe56e057f20f883e"), new User("Cuong", "test2@gmail.com", "e10adc3949ba59abbe56e057f20f883e")};
  public static final Post dummyPost = new Post("title", "post content", 1);
  public static final Post[] dummyPostList = {new Post("title 1", "post content", 1), new Post("title 3", "post content", 1), new Post("title 4", "post content", 1), new Post("title 5", "post content", 1)};
  public static final Comment dummyComment = new Comment("comment content", 1, 1, 0);
  public static final Comment[] dummyCommentList = {new Comment("comment content", 1, 1, 0), new Comment("comment content", 1, 1, 0), new Comment("comment content", 1, 1, 0)};
  public static final Tag dummyTag = new Tag("tag 1");
  public static final Tag[] dummyTagList = {new Tag("tag 1"), new Tag("tag 2"), new Tag("tag 3"),new Tag("tag 4"), new Tag("tag 5")};
  public static final Role dummyRole = new Role("user");
  public static final Role[] dummyRoleList = {new Role("user"), new Role("admin")};
}
