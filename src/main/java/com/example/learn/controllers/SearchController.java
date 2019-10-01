package com.example.learn.controllers;

import com.example.learn.models.Post;
import com.example.learn.models.Search;
import com.example.learn.models.User;
import com.example.learn.services.PostService;
import com.example.learn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

  @Autowired
  UserService userService;

  @Autowired
  PostService postService;

  @RequestMapping("/user")
  public ModelAndView getSearchUserPage(@RequestParam(value = "q", required = false) String queryString, @RequestParam(value = "sortBy", required = false) String sortBy, @RequestParam(value = "page", required = false) Integer page) {
    queryString = queryString !=null ? queryString : "";
    sortBy = sortBy !=null ? sortBy : "a2z";
    page = page != null ? page : 1;

    Search<User> pagination = userService.searchUserInOrderAndPagination(queryString, sortBy, page);

    ModelAndView modelAndView = new ModelAndView("user-search");
    modelAndView.addObject("data", pagination);
    return modelAndView;
  }

//  @RequestMapping("/user/{queryString}")
//  public List<User> searchUserByNameOrEmail(@PathVariable("queryString") String queryString, @RequestParam(value = "sortBy", required = false) String sortBy, @RequestParam(value = "page", required = false) String page) {
//    return userService.searchUserByNameOrEmail(queryString);
//  }

//  @RequestMapping("/user")
//  public List<User> searchUserByNameOrEmail(HttpServletRequest request) {
//    String query = "";
//    String data = request.getQueryString();
//    data = data.replaceFirst("search=", "");
//    return userService.searchUserByNameOrEmail(data);
//  }

  @RequestMapping("/post")
  public List<Post> searchPostByTitleAndContent(@RequestParam(value = "q", required = false) String queryParam) {
    return postService.findPostByTitleAndContentAndTagName(queryParam);
  }

}
