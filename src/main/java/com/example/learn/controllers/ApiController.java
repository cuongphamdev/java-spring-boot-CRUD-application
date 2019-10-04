package com.example.learn.controllers;

import com.example.learn.models.Comment;
import com.example.learn.models.Post;
import com.example.learn.models.Tag;
import com.example.learn.models.User;
import com.example.learn.services.CommentService;
import com.example.learn.services.PostService;
import com.example.learn.services.TagService;
import com.example.learn.services.UserService;
import com.example.learn.utils.CommonUtils;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

  private boolean checkUserDataBlank(String name, String email, String password) {
    if ((name == null && email == null && password == null) || (name.equals("") && email.equals("") && password.equals("")))
      return false;
    return true;
  }

  //  CRUD Api for user
  @RequestMapping(value = "/users")
  private ResponseEntity<Object> getAllUser() {
    List<User> userList = userService.findAllUser();
    try {
      if (userList.size() == 0) throw new Exception("Not data");
      return ResponseEntity.status(HttpStatus.OK)
              .body(userList);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(CommonUtils.getMapErrors(e, "No user data"));
    }
  }


  @RequestMapping(value = "/users", method = RequestMethod.POST)
  private ResponseEntity<Object> createUser(@RequestBody User user) {
    user.setRoleId(1);
    try {
      if (!this.checkUserDataBlank(user.getName(), user.getEmail(), user.getPassword())) {
        throw new Exception("User data is required");
      }
      ;
      User newUser = userService.createNewUser(user.getName(), user.getEmail().toLowerCase(), user.getPassword(), user.getRoleId());
      return ResponseEntity.status(HttpStatus.OK)
              .body(newUser);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(CommonUtils.getMapErrors(e, "You can not create new user"));
    }
  }

  @RequestMapping("/users/{userId}")
  private ResponseEntity<Object> getUserById(@PathVariable(value = "userId") long userId) {
    User foundUser = userService.findUserById(userId);
    try {
      if (foundUser == null) throw new Exception("User not found for id = [" + userId + "]");
      return ResponseEntity.status(HttpStatus.OK)
              .body(foundUser);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(CommonUtils.getMapErrors(e, "User not found"));
    }
  }

  @RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT)
  private ResponseEntity<Object> updateUser(@RequestBody User user, @PathVariable(value = "userId") long userId) {
    if (!this.checkUserDataBlank(user.getName(), user.getEmail(), user.getPassword())) return null;
    try {
      User updateUser = userService.findUserById(userId);
      if (updateUser == null) throw new Exception("User not found for id = [" + userId + "]");
      if (user.getName() != null && !user.getName().equals("")) {
        updateUser.setName(user.getName());
      }
      if (user.getPassword() != null && !user.getPassword().equals("")) {
        updateUser.setPassword(user.getPassword());
      }
      if (user.getEmail() != null && !user.getEmail().equals("")) {
        updateUser.setEmail(user.getEmail());
      }

      User updatedUser = userService.updateUser(updateUser);
      return ResponseEntity.status(HttpStatus.OK)
              .body(updatedUser);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(CommonUtils.getMapErrors(e, "User not found"));
    }
  }

  @RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE)
  private ResponseEntity<Object> deleteUser(@PathVariable("userId") long userId) {
    try {
      User user = userService.findUserById(userId);
      if (user == null) throw new Exception("User not found for id = [" + userId + "]");

      for (Post post : user.getPosts()) {
        this.deleteTags(post.getId());
      }

      long deletedId = userService.deleteUser(userId);
      Map<String, Long> response = new HashMap<>();
      response.put("id", deletedId);
      return ResponseEntity.status(HttpStatus.OK)
              .body(response);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(CommonUtils.getMapErrors(e, "User not found"));
    }
  }

  //  CRUD Api for posts
  @RequestMapping("/posts")
  private ResponseEntity<Object> getAllPost() {
    List<Post> postList = postService.findAllPosts();
    try {
      if (postList.size() == 0) throw new Exception("Not posts");
      return ResponseEntity.status(HttpStatus.OK)
              .body(postList);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(CommonUtils.getMapErrors(e, "No posts data"));
    }
  }

  @RequestMapping("/posts/{postId}")
  private ResponseEntity<Object> findPostById(@PathVariable(value = "postId") long postId) {
    Post foundPost = postService.findPostById(postId);
    try {
      if (foundPost == null) throw new Exception("Post not found for id = [" + postId + "]");
      return ResponseEntity.status(HttpStatus.OK)
              .body(foundPost);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(CommonUtils.getMapErrors(e, "Post not found"));
    }
  }

  private boolean checkValidPostData(String title, String content, Long userId) {
    if (title == null || title.equals("") || content == null || content.equals("") || userId == null) return false;
    return true;
  }

  @RequestMapping(value = "/posts", method = RequestMethod.POST)
  private ResponseEntity<Object> createPostWithPostData(@RequestBody Post post, @RequestParam(value = "tagIds", required = false) List<Long> tagIds) {
    String title = post.getTitle();
    String content = post.getContent();
    Long userId = post.getUserId();
    try {
      if (!this.checkValidPostData(title, content, userId)) throw new Exception("Post data cannot be empty");
      if (userService.findUserById(userId) == null) throw new Exception("User not found with id = [" + userId + "]");
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
      return ResponseEntity.status(HttpStatus.OK)
              .body(postCreated);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(CommonUtils.getMapErrors(e, "Create new user fail"));
    }
  }

  @RequestMapping(value = "/posts/{postId}", method = RequestMethod.PUT)
  private ResponseEntity<Object> updatePostWithPostData(@RequestBody Post post, @PathVariable(value = "postId") long postId, @RequestParam(value = "tagIds", required = false) List<Long> tagIds) {
    try {
      Post newPost = postService.findPostById(postId);
      if (newPost == null) throw new Exception("Post not found");
      String title = post.getTitle();
      String content = post.getContent();
      if (title != null && !title.trim().equals("")) {
        newPost.setTitle(title);
      }
      if (content != null && !content.trim().equals("")) {
        newPost.setContent(title);
      }
      Set<Tag> tagsUpdateList = newPost.getTags();
      Post updatedPost = postService.updatePost(newPost.getId(), newPost.getTitle(), newPost.getContent());
      for (Tag tagDeleted : newPost.getTags()) {
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
      return ResponseEntity.status(HttpStatus.OK)
              .body(newPost);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(CommonUtils.getMapErrors(e, "Update post fail"));
    }
  }

  private void deleteTags(long postId) {
    try {
      Post foundPost = postService.findPostById(postId);
      if (foundPost == null) {
        throw new NotFoundException("Post is not found with id [" + postId + "]");
      } else if (foundPost.getTags() != null) {
        for (Tag tagRemove : foundPost.getTags()) {
          Set<Post> newPosts = tagRemove.getPosts();
          newPosts.remove(foundPost);
          tagRemove.setPosts(newPosts);
          tagService.updateTagPost(tagRemove);
        }
      }
    } catch (Exception e) {
      //
    }
  }

  @RequestMapping(value = "/posts/{postId}", method = RequestMethod.DELETE)
  private ResponseEntity<Object> deletePost(@PathVariable(value = "postId") long postId) {
    try {
      this.deleteTags(postId);
      long deletedPostId = postService.deletePost(postId);
      Map<String, Long> response = new HashMap<>();
      response.put("id", deletedPostId);
      return ResponseEntity.status(HttpStatus.OK)
              .body(response);

    } catch (Exception e) {
      Map<String, String> errors = new HashMap<>();
      errors.put("message", "You cannot delete a post");
      errors.put("error", e.getMessage().toString());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(errors);
    }
  }

  //  CRUD Api for comments
  @RequestMapping(value = "/comments")
  private ResponseEntity<Object> getAllComments() {
    List<Comment> commentList = commentService.findAllComment();
    try {
      if (commentList.size() == 0) throw new Exception("Not data");
      return ResponseEntity.status(HttpStatus.OK)
              .body(commentList);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(CommonUtils.getMapErrors(e, "No comments data"));
    }
  }

  @RequestMapping(value = "/comments/{commentId}")
  private ResponseEntity<Object> getCommentById(@PathVariable(value = "commentId") long commentId) {
    Comment foundComment = commentService.findCommentById(commentId);
    try {
      if (foundComment == null) throw new Exception("Comment not found for id = [" + commentId + "]");
      return ResponseEntity.status(HttpStatus.OK)
              .body(foundComment);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(CommonUtils.getMapErrors(e, "Comment not found"));
    }
  }

  private boolean checkCommentDataValid(String content, Long postId, Long userId, Long parentId) {
    if (content == null || content.trim().equals("") || postId == null || userId == null ||
            (parentId != 0 && commentService.findCommentById(parentId) == null) ||
            postService.findPostById(postId) == null ||
            userService.findUserById(userId) == null
    ) {
      return false;
    }
    return true;
  }

  @RequestMapping(value = "/comments", method = RequestMethod.POST)
  private ResponseEntity<Object> createComment(@RequestBody Comment comment) {
    try {
      String content = comment.getContent();
      Long postId = comment.getPostId();
      Long userId = comment.getUserId();
      Long parentId = comment.getParentId() == null ? 0 : comment.getParentId();
      if (checkCommentDataValid(content, postId, userId, parentId)) {
        Comment createdComment = commentService.createComment(content, postId, userId, parentId);
        createdComment.setUser(userService.findUserById(createdComment.getUserId()));
        return ResponseEntity.status(HttpStatus.OK)
                .body(createdComment);
      }
      return null;
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(CommonUtils.getMapErrors(e, "Create comment fail"));
    }
  }

  @RequestMapping(value = "/comments/{commentId}", method = RequestMethod.PUT)
  private ResponseEntity<Object> updateComment(@RequestBody Comment comment, @PathVariable("commentId") long commentId) {
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
        Comment updatedComment = commentService.updateComment(commentId, content);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedComment);
      } else {
        throw new Exception("Data input cannot empty");
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(CommonUtils.getMapErrors(e, "Update fail"));
    }
  }

  @RequestMapping(value = "/comments/{commentId}", method = RequestMethod.DELETE)
  private ResponseEntity<Object> deleteComment(@PathVariable("commentId") long commentId) {
    try {
      Comment foundComment = commentService.findCommentById(commentId);
      if (foundComment == null) throw new Exception("Comment not found for id = [" + commentId + "]");
      long deletedId = commentService.deleteComment(commentId);
      Map<String, Long> response = new HashMap<>();
      response.put("id", deletedId);
      return ResponseEntity.status(HttpStatus.OK)
              .body(response);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(CommonUtils.getMapErrors(e, "Comment not found"));
    }
  }

  //  CRUD Api for tags
  @RequestMapping(value = "/tags")
  private ResponseEntity<Object> getAllTag() {
    List<Tag> list = tagService.getAllTags();
    try {
      if (list.size() == 0) throw new Exception("Not tags");
      return ResponseEntity.status(HttpStatus.OK)
              .body(list);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(CommonUtils.getMapErrors(e, "No tas data"));
    }
  }

  @RequestMapping(value = "/tags/{tagId}")
  private ResponseEntity<Object> getTagById(@PathVariable("tagId") long tagId) {
    Tag found = tagService.getTagById(tagId);
    try {
      if (found == null) throw new Exception("Post not found for id = [" + tagId + "]");
      return ResponseEntity.status(HttpStatus.OK)
              .body(found);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(CommonUtils.getMapErrors(e, "Tag not found"));
    }
  }

  @RequestMapping(value = "/tags", method = RequestMethod.POST)
  private ResponseEntity<Object> createTag(@RequestBody Tag tag) {
    try {
      String tagName = tag.getName();
      if (tagName == null || tagName.trim().equals("")) {
        throw new Exception("Tag data cannot empty!");
      }
      Tag tagCreated = tagService.createNewTag(tagName);
      return ResponseEntity.status(HttpStatus.OK)
              .body(tagCreated);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(CommonUtils.getMapErrors(e, "Tag create fail"));
    }
  }

  @RequestMapping(value = "/tags/{tagId}", method = RequestMethod.PUT)
  private ResponseEntity<Object> updateTag(@RequestBody Tag tag, @PathVariable("tagId") long tagId) {
    try {
      String tagName = tag.getName();
      if (tagName == null || tagName.trim().equals("") || tagService.getTagById(tagId) == null) {
        throw new Exception("Tag data cannot empty!");
      }
      Tag tagUpdated = tagService.updateTag(tagId, tagName);
      return ResponseEntity.status(HttpStatus.OK)
              .body(tagUpdated);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(CommonUtils.getMapErrors(e, "Tag update fail"));
    }
  }

  @RequestMapping(value = "/tags/{tagId}", method = RequestMethod.DELETE)
  private ResponseEntity<Object> deleteTag(@PathVariable("tagId") long tagId) {
    try {
      Tag tagFound = tagService.getTagById(tagId);
      if (tagFound == null) throw new NotFoundException("Tag is not found with id [" + tagId + "]");
      long tagDeletedId = tagService.deleteTag(tagId);
      Map<String, Long> response = new HashMap<>();
      response.put("id", tagDeletedId);
      return ResponseEntity.status(HttpStatus.OK)
              .body(response);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(CommonUtils.getMapErrors(e, "Tag not found"));
    }
  }
}
