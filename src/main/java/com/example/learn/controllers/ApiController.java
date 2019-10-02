package com.example.learn.controllers;

import com.example.learn.models.Comment;
import com.example.learn.models.Post;
import com.example.learn.models.Tag;
import com.example.learn.models.User;
import com.example.learn.services.CommentService;
import com.example.learn.services.PostService;
import com.example.learn.services.TagService;
import com.example.learn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class ApiController {

  @Autowired
  private UserService userService;

  @Autowired
  private CommentService commentService;

  @Autowired
  private PostService postService;

  @Autowired
  private TagService tagService;

  private boolean checkUserDataBlank (String name, String email, String password) {
    if ((name == null && email == null && password == null) || (name.equals("") && email.equals("") && password.equals("")) ) return false;
    return true;
  }

  //  CRUD Api for user
  @RequestMapping(value = "users")
  private List<User> getAllUser() {
    return userService.findAllUser();
  }

  @RequestMapping(value = "/users", method = RequestMethod.POST)
  private User createUser (@ModelAttribute User user) {
    user.setRoleId(1);
    if (!this.checkUserDataBlank(user.getName(), user.getEmail(), user.getPassword())) return null;
    try {
      return userService.createNewUser(user.getName(), user.getEmail().toLowerCase(), user.getPassword(), user.getRoleId());
    } catch (Exception e) {
      return  null;
    }
  }

  @RequestMapping("/users/{userId}")
  private User getUserById (@PathVariable(value = "userId") long userId) {
    return userService.findUserById(userId);
  }

  @RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT)
  private User updateUser (@ModelAttribute User user, @PathVariable(value = "userId") long userId) {
    if (!this.checkUserDataBlank(user.getName(), user.getEmail(), user.getPassword())) return null;
    try {
      User updateUser = userService.findUserById(userId);
      if (user.getName() != null && !user.getName().equals("")) {
        updateUser.setName(user.getName());
      }
      if (user.getPassword() != null && !user.getPassword().equals("")) {
        updateUser.setPassword(user.getPassword());
      }
      if (user.getEmail() != null && !user.getEmail().equals("")) {
        updateUser.setEmail(user.getEmail());
      }
      return userService.updateUser(updateUser);
    } catch (Exception e) {
      return  null;
    }
  }

  @RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE)
  private long deleteUser (@PathVariable("userId") long userId) {
    try {
      return userService.deleteUser(userId);
    } catch (Exception e) {
      return 0;
    }
  }

  //  CRUD Api for posts
  @RequestMapping("/posts")
  private List<Post> getAllPost () {
    return postService.findAllPosts();
  }

  @RequestMapping("/posts/{postId}")
  private Post findPostById (@PathVariable(value = "postId") long postId) {
    return postService.findPostById(postId);
  }

  private boolean checkValidPostData (String title, String content, Long userId) {
    if (title == null || title.equals("") || content == null || content.equals("") || userId == null) return false;
    if (userService.findUserById(userId) == null) return false;
    return true;
  }

  @RequestMapping(value = "/posts", method = RequestMethod.POST)
  private Post createPostWithPostData (@ModelAttribute Post post, @RequestParam(value = "tagIds", required = false) List<Long> tagIds) {
    String title = post.getTitle();
    String content = post.getContent();
    Long userId = post.getUserId();
    if (this.checkValidPostData(title, content, userId)) {
      Post postCreated = postService.createNewPost(title, content, userId);
      Set<Tag> tagsAdd = new HashSet<>();
      if (tagIds != null) {
        for (long tagId : tagIds) {
          Tag tag = tagService.getTagById(tagId);
          Set<Post> newPosts = tag.getPosts();
          newPosts.add(postCreated);
          tag.setPosts(newPosts);
          tagService.updateTagPost(tag);
          tagsAdd.add(tag);
        }
      }
      postCreated.setTags(tagsAdd);
      return postCreated;
    }
    return null;
  }

  @RequestMapping(value = "/posts/{postId}", method = RequestMethod.PUT)
  private Post updatePostWithPostData (@ModelAttribute Post post, @PathVariable(value = "postId") long postId, @RequestParam(value = "tagIds", required = false) List<Long> tagIds) {
    Post newPost = postService.findPostById(postId);
    if (newPost == null) return null;
    String title = post.getTitle();
    String content = post.getContent();
    if (title != null && !title.trim().equals("")) {
      newPost.setTitle(title);
    }
    if (content != null && !content.trim().equals("")) {
      newPost.setContent(title);
    }
    Set<Tag> tagsUpdateList = newPost.getTags();
      Post updatedPost = postService.updatePost(newPost.getId(), newPost.getTitle(), newPost.getContent()) ;
      for (Tag tagDeleted: newPost.getTags()) {
        Set<Post> postList = tagDeleted.getPosts();
        postList.remove(updatedPost);
        tagDeleted.setPosts(postList);
        tagService.updateTagPost(tagDeleted);
      }

    if (tagIds != null) {
      for (long tagId : tagIds) {
        Tag tag = tagService.getTagById(tagId);
        Set<Post> newPosts = tag.getPosts();
        newPosts.add(updatedPost);
        tag.setPosts(newPosts);
        tagService.updateTagPost(tag);
        tagsUpdateList.add(tag);
      }
    } else {
      tagsUpdateList = null;
    }
    newPost.setTags(tagsUpdateList);
    return newPost;
  }

  @RequestMapping(value = "/posts/{postId}", method = RequestMethod.DELETE)
  private long deletePost (@PathVariable(value = "postId") long postId) {
    try {
      return postService.deletePost(postId);
    } catch (Exception e) {
      return 0;
    }
  }

  //  CRUD Api for comments
  @RequestMapping(value = "/comments")
  private List<Comment> getAllComments () {
    return commentService.findAllComment();
  }

  @RequestMapping(value = "/comments/{commentId}")
  private Comment getCommentById (@PathVariable(value = "commentId") long commentId) {
    return commentService.findCommentById(commentId);
  }

  private boolean checkCommentDataValid (String content, Long postId, Long userId, Long parentId) {
    if (content == null || content.trim().equals("") || postId == null || userId == null ||
            (parentId != 0 && commentService.findCommentById(parentId) == null) ||
            postService.findPostById(postId) == null||
            userService.findUserById(userId) == null
    ) {
      return false;
    }
    return true;
  }

  @RequestMapping(value = "/comments", method = RequestMethod.POST)
  private Comment createComment (@ModelAttribute Comment comment) {
    String content = comment.getContent();
    Long postId = comment.getPostId();
    Long userId = comment.getUserId();
    Long parentId = comment.getParentId() == null ? 0 : comment.getParentId();
    if (checkCommentDataValid(content, postId, userId, parentId)) {
      Comment createdComment = commentService.createComment(content, postId, userId, parentId);
      createdComment.setUser(userService.findUserById(createdComment.getUserId()));
      return createdComment;
    }
    return null;
  }

  @RequestMapping(value = "/comments/{commentId}",  method = RequestMethod.PUT)
  private Comment updateComment (@ModelAttribute Comment comment, @PathVariable("commentId") long commentId) {
    try {
      String content = comment.getContent();
      long userId = comment.getUserId();
      Comment updateComment = commentService.findCommentById(commentId);
      if (updateComment != null &&
              content != null &&
              !content.trim().equals("") &&
              userId != 0 &&
              userId == updateComment.getUserId()
      ) {
        return commentService.updateComment(commentId, content);
      }
    } catch (Exception e) {
      return null;
    }
    return null;
  }

  @RequestMapping(value = "/comments/{commentId}",  method = RequestMethod.DELETE)
  private long deleteComment (@PathVariable("commentId") long commentId) {
    try {
      return commentService.deleteComment(commentId);
    } catch (Exception e) {
      return 0;
    }
  }

  //  CRUD Api for tags
  @RequestMapping(value = "/tags")
  private List<Tag> getAllTag () {
    return tagService.getAllTags();
  }

  @RequestMapping(value = "/tags/{tagId}")
  private Tag getTagById (@PathVariable("tagId") long tagId) {
    return tagService.getTagById(tagId);
  }

  @RequestMapping(value = "/tags", method = RequestMethod.POST)
  private Tag createTag (@ModelAttribute Tag tag) {
    String tagName = tag.getName();
    if (tagName == null || tagName.trim().equals("")) {
      return null;
    }
    Tag tagCreated = tagService.createNewTag(tagName);
    return tagCreated;
  }

  @RequestMapping(value = "/tags/{tagId}", method = RequestMethod.PUT)
  private Tag updateTag (@ModelAttribute Tag tag, @PathVariable("tagId") long tagId) {
    String tagName = tag.getName();
    if (tagName == null || tagName.trim().equals("") || tagService.getTagById(tagId) == null) {
      return null;
    }
    Tag tagCreated = tagService.updateTag(tagId, tagName);
    return tagCreated;
  }

  @RequestMapping(value = "/tags/{tagId}", method = RequestMethod.DELETE)
  private long deleteTag (@PathVariable("tagId") long tagId) {
    try {
      return tagService.deleteTag(tagId);
    } catch (Exception e) {
      return 0;
    }
  }
}
