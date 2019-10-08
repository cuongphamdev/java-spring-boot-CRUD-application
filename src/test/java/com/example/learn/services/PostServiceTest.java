package com.example.learn.services;

import com.example.learn.Utils;
import com.example.learn.daos.PostDAO;
import com.example.learn.daos.UserDAO;
import com.example.learn.models.Post;
import com.example.learn.models.Search;
import com.example.learn.services.impl.PostServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {

  @Mock
  PostDAO postDAO;

  @Mock
  UserDAO userDAO;

  @InjectMocks
  PostService postService = new PostServiceImpl();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @BeforeEach
  public void setupEach() {
    when(postDAO.findAll()).thenReturn(Arrays.asList(ServiceDataTest.dummyPostList));

    when(postDAO.countPostByUserId(0)).thenReturn((long) 0);
    when(postDAO.countPostByUserId(1)).thenReturn((long) ServiceDataTest.dummyPostList.length);

    when(postDAO.findById(anyLong())).thenAnswer(invocation -> {
      long postId = invocation.getArgument(0);

      if (Utils.checkValidBetween(postId, 0, ServiceDataTest.dummyPostList.length)) {
        return ServiceDataTest.dummyPostList[(int) postId - 1];
      }
      return null;
    });

    when(userDAO.findById(0)).thenReturn(null);
    when(userDAO.findById(1)).thenReturn(ServiceDataTest.dummyUser);

    when(postDAO.create(any())).thenAnswer(
            invocation -> {
              Post postData = (Post) invocation.getArgument(0);
              if (postData.getTitle().equals("") || postData.getContent().equals("")) {
                return null;
              }
              return new Post(postData.getTitle(), postData.getContent(), postData.getUserId());
            }
    );

    when(postDAO.update(any())).thenAnswer(
            invocationOnMock -> {
              Post postData = (Post) invocationOnMock.getArgument(0);
              if (postData.getTitle().equals("") || postData.getContent().equals("")) {
                return null;
              }
              return new Post(postData.getTitle(), postData.getContent(), postData.getUserId());
            }
    );

    when(postDAO.delete(anyLong())).thenAnswer(
            invocationOnMock -> {
              long postId = (long) invocationOnMock.getArgument(0);
              if (postId == 0) {
                return (long) 0;
              } else {
                return postId;
              }
            }
    );

    when(postDAO.findAllPostPagination(anyInt())).thenAnswer(
            invocationOnMock -> {
              int userId = (int) invocationOnMock.getArgument(0);
              if (userId == 0) {
                return null;
              }
              return Arrays.asList(ServiceDataTest.dummyPostList);
            }
    );

    when(postDAO.findAllPostPagination(anyInt())).thenAnswer(
            invocationOnMock -> {
              int page = (int) invocationOnMock.getArgument(0);
              if (page == 0) {
                return null;
              }
              return Arrays.asList(ServiceDataTest.dummyPostList);
            }
    );

    when(userDAO.findAll()).thenReturn(Arrays.asList(ServiceDataTest.dummyUserList));
    when(postDAO.searchPostByTitleAndContentAndTagNameWithUserId(anyString(), anyLong(), anyString(), anyInt())).thenAnswer(invocation -> {
      String query = invocation.getArgument(0);
      long userId = invocation.getArgument(1);
      String order = invocation.getArgument(2);
      int page = invocation.getArgument(3);
      List<Post> postList = new ArrayList<>();

      for (Post eachPost : ServiceDataTest.dummyPostList) {
        if (eachPost.getTitle().contains(query) || eachPost.getContent().contains(query)) {
          postList.add(eachPost);
        }
      }

      if (order.equals("a2z")) {
        Collections.sort(postList, Utils.comparePostByTitleASC);
      } else {
        Collections.sort(postList, Utils.comparePostByTitleDESC);
      }
      if (page > 1 || userId == 0) {
        postList.removeAll(postList);
      }
      Search<Post> response = new Search<>(postList, postList.size(), 1, query, 1);
      return response;
    });

    when(postDAO.searchPostByTitleAndContentAndNameUserWithSortAndPageBreak(anyString(), anyString(), anyInt(), anyInt(), anyInt())).thenAnswer(invocation -> {
      String query = invocation.getArgument(0);
      String order = invocation.getArgument(1);
      int page = invocation.getArgument(2);
      int pageBreak = invocation.getArgument(3);
      int tagId = invocation.getArgument(4);
      List<Post> postList = new ArrayList<>();

      for (Post eachPost : ServiceDataTest.dummyPostList) {
        if (eachPost.getTitle().contains(query) || eachPost.getContent().contains(query)) {
          postList.add(eachPost);
        }
        if (postList.size() >= pageBreak) {
          break;
        }
      }

      if (order.equals("a2z")) {
        Collections.sort(postList, Utils.comparePostByTitleASC);
      } else {
        Collections.sort(postList, Utils.comparePostByTitleDESC);
      }
      if (page > 1 || tagId > 10) {
        postList.removeAll(postList);
      }

      if (page != 1) {
        postList.removeAll(postList);
      }
      Search<Post> response = new Search<>(postList, (postList.size() - pageBreak) * 1, 1, query, 1);
      return response;
    });


  }

  @DisplayName("findAllPostsSuccess return list post")
  @Test
  void findAllPostsSuccess() {
    List<Post> result = postService.findAllPosts();
    assertEquals(Arrays.asList(ServiceDataTest.dummyPostList), result);
  }

  @DisplayName("findPostByIdSuccess return a post")
  @Test
  void findPostByIdSuccess() {
    long input = 1;
    Post result = postService.findPostById(input);
    assertEquals(ServiceDataTest.dummyPostList[(int) input - 1], result);
  }

  @DisplayName("findPostByIdFail return null")
  @Test
  void findPostByIdFail() {
    Post result = postService.findPostById(0);
    assertNull(result);
  }

  @DisplayName("createNewPostSuccess success and return an new post data")
  @Test
  void createNewPostSuccess() {
    Post result = postService.createNewPost(ServiceDataTest.dummyPost.getTitle(), ServiceDataTest.dummyPost.getContent(), 1);
    assertEquals(ServiceDataTest.dummyPost.getTitle(), result.getTitle());
  }

  @DisplayName("createNewPost fail and return null")
  @Test
  void createNewPostFail() {
    Post result = postService.createNewPost("", ServiceDataTest.dummyPost.getContent(), 0);
    assertEquals(null, result);
  }

  @DisplayName("updatePost success and return post data")
  @Test
  void updatePostSuccess() {
    Post result = postService.updatePost(1, ServiceDataTest.dummyPost.getTitle(), ServiceDataTest.dummyPost.getContent());
    assertEquals(ServiceDataTest.dummyPost.getTitle(), result.getTitle());
  }

  @DisplayName("updatePost fail and return null")
  @Test
  void updatePostFail() {
    Post result = postService.updatePost(0, ServiceDataTest.dummyPost.getTitle(), ServiceDataTest.dummyPost.getContent());
    assertEquals(null, result);
  }

  @DisplayName("deletePost success and return deleted post id")
  @Test
  private void deletePostSuccess() {
    long deletedPostId = postService.deletePost(1);
    assertEquals(deletedPostId, 1);
  }

  @DisplayName("deletePost fail and return 0")
  @Test
  void deletePostFail() {
    long deletedPostId = postService.deletePost(0);
    assertEquals(0, deletedPostId);
  }

  @DisplayName("findAllPostByUserIdAndPagination success and return list post")
  @Test
  private void findAllPostByUserIdAndPaginationSuccess() {
    List<Post> result = postService.findAllPostByUserIdAndPagination(1, 1);
    assertEquals(ServiceDataTest.dummyPostList, result);
  }

  @DisplayName("findAllPostByUserIdAndPagination fail and return null")
  @Test
  void findAllPostByUserIdAndPaginationFail() {
    List<Post> result = postService.findAllPostByUserIdAndPagination(0, 1);
    assertEquals(0, result.size());
  }

  @DisplayName("createPost success and return new post data")
  @Test
  private void createPostSuccess() {
    Post result = postService.createPost(ServiceDataTest.dummyPost);
    assertEquals(ServiceDataTest.dummyPost, result);
  }

  @DisplayName("createPost fail and return null")
  @Test
  void createPostFail() {
    Post newPostData = new Post("", ServiceDataTest.dummyPost.getContent(), 0);
    Post result = postService.createPost(newPostData);
    assertEquals(null, result);
  }

  @DisplayName("findAllPostPagination success and return list post")
  @Test
  void findAllPostPaginationSuccess() {
    List<Post> result = postService.findAllPostPagination(1);
    assertEquals(Arrays.asList(ServiceDataTest.dummyPostList), result);
  }

  @DisplayName("findAllPostPagination fail and return null")
  @Test
  private void findAllPostPaginationFail() {
    List<Post> result = postService.findAllPostPagination(0);
    assertEquals(null, result);
  }

  @DisplayName("countPostByUserId success and return size of list post")
  @Test
  void countPostByUserIdSuccess() {
    long result = postService.countPostByUserId(1);
    assertEquals(ServiceDataTest.dummyPostList.length, result);
  }

  @DisplayName("countPostByUserId fail and return 0")
  @Test
  void countPostByUserIdFail() {
    long result = postService.countPostByUserId(0);
    assertEquals(0, result);
  }

  @DisplayName("findPostByTitleAndContentAndTagNameWithUserId success")
  @Test
  void findPostByTitleAndContentAndTagNameWithUserIdSuccess() {
    Search<Post> result = postService.findPostByTitleAndContentAndTagNameWithUserId("title", 1, "a2z", 1);
    assertNotEquals(0, result.getListItems().size());
  }

  @DisplayName("findPostByTitleAndContentAndTagNameWithUserId fail with wrong userId")
  @Test
  void findPostByTitleAndContentAndTagNameWithUserIdFailWithWrongUserId() {
    Search<Post> result = postService.findPostByTitleAndContentAndTagNameWithUserId("title", 0, "a2z", 1);
    assertEquals(0, result.getListItems().size());
  }

  @DisplayName("findPostByTitleAndContentAndTagNameWithUserId fail with wrong queryString")
  @Test
  void findPostByTitleAndContentAndTagNameWithUserIdFailWithWrongQuery() {
    Search<Post> result = postService.findPostByTitleAndContentAndTagNameWithUserId("title-11", 1, "a2z", 1);
    assertEquals(0, result.getListItems().size());
  }

  @DisplayName("searchPostByTitleAndContentAndNameUserWithSortAndPageBreak success")
  @Test
  void searchPostByTitleAndContentAndNameUserWithSortAndPageBreakSuccess() {
    Search<Post> result = postService.searchPostByTitleAndContentAndNameUserWithSortAndPageBreak("title", "a2z", 1, 4, 0);
    assertEquals(4, result.getListItems().size());
  }

  @DisplayName("searchPostByTitleAndContentAndNameUserWithSortAndPageBreak success")
  @Test
  void searchPostByTitleAndContentAndNameUserWithSortAndPageBreakSuccessWithPageBreak() {
    Search<Post> result = postService.searchPostByTitleAndContentAndNameUserWithSortAndPageBreak("title", "a2z", 1, 2, 0);
    assertEquals(2, result.getListItems().size());
  }

  @DisplayName("searchPostByTitleAndContentAndNameUserWithSortAndPageBreak fail with wrong tagId")
  @Test
  void searchPostByTitleAndContentAndNameUserWithSortAndPageBreakFailWithWrongTagId() {
    Search<Post> result = postService.searchPostByTitleAndContentAndNameUserWithSortAndPageBreak("title", "a2z", 1, 1, 11);
    assertEquals(0, result.getListItems().size());
  }

  @DisplayName("searchPostByTitleAndContentAndNameUserWithSortAndPageBreak fail with wrong string q")
  @Test
  void searchPostByTitleAndContentAndNameUserWithSortAndPageBreakFailWithWrongQueryString() {
    Search<Post> result = postService.searchPostByTitleAndContentAndNameUserWithSortAndPageBreak("wrong", "a2z", 1, 1, 0);
    assertEquals(0, result.getListItems().size());
  }
}
