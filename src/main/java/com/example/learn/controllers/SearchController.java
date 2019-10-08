package com.example.learn.controllers;

import com.example.learn.models.Post;
import com.example.learn.models.Search;
import com.example.learn.models.Tag;
import com.example.learn.models.User;
import com.example.learn.services.PostService;
import com.example.learn.services.TagService;
import com.example.learn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController<T> {

  @Autowired
  UserService userService;

  @Autowired
  @Qualifier("PostService2")
  PostService postService;

  @Autowired
  TagService tagService;

  @RequestMapping("/user")
  private ModelAndView getSearchUserPage(@RequestParam(value = "q", required = false) String queryString,
                                         @RequestParam(value = "sortBy", required = false) String sortBy,
                                         @RequestParam(value = "page", required = false) Integer page) {
    queryString = queryString != null ? queryString.trim() : "";
    sortBy = sortBy != null ? sortBy.trim() : "a2z";
    page = page != null ? page : 1;
    Search<User> pagination = userService.searchUserInOrderAndPagination(queryString, sortBy, page);
    ModelAndView modelAndView = new ModelAndView("user-search");
    modelAndView.addObject("data", pagination);
    return modelAndView;
  }

  @RequestMapping("/post")
  private ModelAndView getSearchBoxPage(@RequestParam(value = "q", required = false) String postQuery,
                                        @RequestParam(value = "sortBy", required = false) String sortBy,
                                        @RequestParam(value = "page", required = false) Integer page,
                                        @RequestParam(value = "tagId", required = false) Integer tagId,
                                        @RequestParam(value = "break", required = false) Integer pageBreak) {

    postQuery = postQuery != null ? postQuery.trim() : "";
    sortBy = sortBy != null ? sortBy.trim() : "a2z";
    page = page != null ? page : 1;
    pageBreak = pageBreak != null ? pageBreak : 5;
    tagId = tagId != null ? tagId : 0;

    List<Tag> listTags = tagService.getAllTags();
    Search<Post> searchData = postService.searchPostByTitleAndContentAndNameUserWithSortAndPageBreak(postQuery, sortBy, page, pageBreak, tagId);
    ModelAndView modelAndView = new ModelAndView("post-search");
    modelAndView.addObject("searchPostData", searchData);
    modelAndView.addObject("tags", listTags);
    return modelAndView;
  }
}
