package com.example.learn.controllers;

import com.example.learn.models.Comment;
import com.example.learn.models.Post;
import com.example.learn.models.Tag;
import com.example.learn.services.CommentService;
import com.example.learn.services.PostService;
import com.example.learn.services.TagService;
import com.example.learn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/posts")
public class PostController {

  @Autowired
  private PostService postService;

  @Autowired
  private UserService userService;

  @Autowired
  private CommentService commentService;

  @Autowired
  private TagService tagService;

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public long deletePostById(@PathVariable(value = "id") long postId, HttpServletRequest request) {
    long currentUserId = userService.getCurrentUserId(request);
    Post foundPost = postService.findPostById(postId);
    if (foundPost.getUserId() != currentUserId) {
      return 0;
    }
    if (foundPost.getTags() != null) {
      for (Tag tagRemove : foundPost.getTags()) {
        Set<Post> newPosts = tagRemove.getPosts();
        newPosts.remove(foundPost);
        tagRemove.setPosts(newPosts);
        tagService.updateTagPost(tagRemove);
      }
    }
    return postService.deletePost(postId);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public Post updatePost(@PathVariable(value = "id") long postId, @ModelAttribute Post post, HttpServletRequest request) {
    String tags = request.getParameter("tags");
    List<String> tagIdsString = tags == null ? new ArrayList<>() : Arrays.asList((tags.split(",")));
    List<Integer> tagIds = new ArrayList<>();
    for (String tag: tagIdsString) {
      if (!tag.equals("")) {
        tagIds.add(Integer.valueOf(tag));
      }
    }
    long currentUserId = userService.getCurrentUserId(request);
    Post foundPost = postService.findPostById(postId);
    if (foundPost.getUserId() != currentUserId) {
      return null;
    }
    String title = post.getTitle();
    String content = post.getContent();
    Set<Tag> tagsUpdateList = foundPost.getTags();
    for (Tag tagDeleted : foundPost.getTags()) {
      Set<Post> postList = tagDeleted.getPosts();
      postList.remove(foundPost);
      tagDeleted.setPosts(postList);
      tagService.updateTagPost(tagDeleted);
    }
    if (tagIds != null) {
      for (long tagId : tagIds) {
        Tag tag = tagService.getTagById(tagId);
        Set<Post> newPosts = tag.getPosts();
        newPosts.add(foundPost);
        tag.setPosts(newPosts);
        tagService.updateTagPost(tag);
        tagsUpdateList.add(tag);
      }
    }

    Post postUpdated = postService.updatePost(postId, title, content);
    return postService.findPostById(postUpdated.getId());
  }

  @RequestMapping(value = "/{id}")
  public ModelAndView getPostById(@PathVariable(value = "id") long postId) {
    Post currentPost = postService.findPostById(postId);
    List<Comment> commentLists = commentService.findAllRootCommentByPostId(postId);
    ModelAndView modelAndView = new ModelAndView("post");
    modelAndView.addObject("post", currentPost);
    modelAndView.addObject("comments", commentLists.size() > 0 ? commentLists : null);

    return modelAndView;
  }

  @RequestMapping(method = RequestMethod.POST)
  public ModelAndView createPost(@ModelAttribute Post post, HttpServletRequest request, @RequestParam(value = "tagIds", required = false) List<Long> tagIds) {
    try {
      long currentUserId = userService.getCurrentUserId(request);
      String title = post.getTitle();
      String content = post.getContent();
      if (currentUserId != 0) {
        Post postCreated = postService.createNewPost(title, content, currentUserId);
        if (tagIds != null) {
          for (long tagId : tagIds) {
            Tag tag = tagService.getTagById(tagId);
            Set<Post> newPosts = tag.getPosts();
            newPosts.add(postCreated);
            tag.setPosts(newPosts);
            tagService.updateTagPost(tag);
          }
        }
      }
    } catch (Exception e) {
      System.out.println("Error" + e);
    } finally {
      return new ModelAndView("redirect:/");
    }
  }
}
