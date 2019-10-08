package com.example.learn.services;

import com.example.learn.Utils;
import com.example.learn.daos.PostDAOV2;
import com.example.learn.daos.UserDAO;
import com.example.learn.daos.impl.PostDAOImplV2;
import com.example.learn.models.Post;
import com.example.learn.models.Search;
import com.example.learn.models.User;
import com.example.learn.services.impl.PostServiceImplV2;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceV2Test {

  private List<Post> postLists = new ArrayList<>();
  private List<User> userList = new ArrayList<>();

  @Mock
  PostDAOV2 postDAO = new PostDAOImplV2();

  @Spy
  CriteriaQuery<Post> criteriaQuery;
//
//  @Spy
//  EntityManager entityManager;
//
//  @Spy
//  Root<Post> root;
//
//  @Spy
//  CriteriaBuilder builder;

  @Mock
  UserDAO userDAO;

  @InjectMocks
  PostService postService = new PostServiceImplV2();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @BeforeEach
  public void setupForEach() {

    User u1 = new User("Cuong", "cuongpham@gmail.com", "123456");
    User u2 = new User("Duy", "duy@gmail.com", "123456");
    User u3 = new User("Hien", "hien@gmail.com", "123456");
    u1.setId(1);
    u2.setId(2);
    u3.setId(3);
    User[] users = {u1, u2, u3};
    userList.addAll(Arrays.asList(users));

    Post p1 = new Post("title 1", "content 1", u1);
    Post p2 = new Post("title 2", "content 2", u1);
    Post p3 = new Post("title 3", "content 3", u1);
    Post p4 = new Post("title 4", "content 4", u1);
    Post p5 = new Post("title 5", "content 5", u1);
    Post p6 = new Post("title 6", "content 6", u1);
    Post p7 = new Post("title 7", "content 7", u2);
    Post p8 = new Post("title 8", "content 8", u2);
    Post p9 = new Post("title 9", "content 9", u2);
    Post p10 = new Post("title 10", "content 10", u3);
    Post p11 = new Post("title 11", "content 11", u3);
    Post[] posts = {p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11};
    postLists.addAll(Arrays.asList(posts));

//    when(entityManager.getCriteriaBuilder()).thenReturn(builder);
//    when(builder.createQuery(Post.class)).thenReturn(criteriaQuery);
//    when(criteriaQuery.from(Post.class)).thenReturn(root);
//    when(criteriaQuery.where((Expression<Boolean>) any())).thenReturn(criteriaQuery);
//    when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
//    when(criteriaQuery.groupBy((List<Expression<?>>) any())).thenReturn(criteriaQuery);

    when(postDAO.findAll()).thenReturn(postLists);

    when(postDAO.findById(anyLong())).thenAnswer(invocation -> {
      long postId = invocation.getArgument(0);
      if (Utils.checkValidBetween((long) postId, 0, postLists.size())) {
        return postLists.get((int) postId - 1);
      }
      return null;
    });

    when(userDAO.findById(anyLong())).thenAnswer(invocation -> {
      long userId = invocation.getArgument(0);
      if (Utils.checkValidBetween((long) userId, 0, userList.size())) {
        User response = userList.get((int) userId - 1);
        return response;
      }
      return null;
    });

    when(postDAO.create(any())).thenAnswer(invocation -> {
      Post post = invocation.getArgument(0);
      return post;
    });

    when(postDAO.update(any())).thenAnswer(invocation -> {
      Post post = invocation.getArgument(0);
      return post;
    });

    when(postDAO.delete(1L)).thenReturn(1L);
    when(postDAO.delete(anyLong())).thenReturn(0L);

    when(postDAO.countPostByUserId(1)).thenReturn(10L);
    when(postDAO.countPostByUserId(0)).thenReturn(0L);

    when(postDAO.findAllPostByUserIdAndPagination(1, 1, 5)).thenReturn(postLists.subList(0, 5));
    when(postDAO.findAllPostByUserIdAndPagination(1, 5, 5)).thenReturn(postLists.subList(5, 6));

    when(postDAO.findAllPostPagination(1, 5)).thenReturn(postLists.subList(0, 5));
    when(postDAO.findAllPostPagination(5, 5)).thenReturn(postLists.subList(5, 10));
    when(postDAO.findAllPostPagination(10, 5)).thenReturn(null);

    when(postDAO.getPostSearchTitleNameTagViaUserIdCriteriaQuery(anyString(), anyLong(), anyString())).
            thenReturn(criteriaQuery);

//    when(criteriaQuery.select(any())).thenReturn(criteriaQuery);
//    when(criteriaQuery.where((Expression<Boolean>) any())).thenReturn(criteriaQuery);
//    when(criteriaQuery.groupBy((List<Expression<?>>) any())).thenReturn(criteriaQuery);
//    when(criteriaQuery.orderBy((List<Order>) any())).thenReturn(criteriaQuery);


    when(postDAO.countPostsByQuery(criteriaQuery)).thenReturn(10);
    when(postDAO.ListPostByQuery(criteriaQuery, 1, 5)).thenReturn(postLists.subList(0, 5));


  }

  @Test
  @DisplayName("findAllPosts success")
  public void findAllPostsTestSuccess() {
    List<Post> result = postService.findAllPosts();
    assertEquals(postLists.size(), result.size());
  }

  @Test
  @DisplayName("findPostByIdSuccess")
  public void findPostByIdSuccess() {
    Post result = postService.findPostById(1L);
    assertEquals(postLists.get(0), result);
  }

  @Test
  @DisplayName("findPostById Fail with Invalid Id")
  public void findPostByIdFailByInvalidId() {
    Post result = postService.findPostById(12L);
    assertEquals(null, result);
  }

  @Test
  @DisplayName("createNewPost success")
  public void createNewPostSuccess() {
    Post result = postService.createNewPost("test title", "content", 1);
    assertNotNull(result);
  }

  @Test
  @DisplayName("createNewPost fail with invalid userId")
  public void createNewPostFail() {
    Post result = postService.createNewPost("test title", "content", 5);
    assertNull(result);
  }

  @Test
  @DisplayName("UpdatePost success")
  public void updatePostSuccess() {
    Post result = postService.updatePost(1, "new Title", "new content");
    assertNotNull(result);
  }

  @Test
  @DisplayName("UpdatePost fail with invalid postId")
  public void updatePostFail() {
    Post result = postService.updatePost(15, "new Title", "new content");
    assertNull(result);
  }

  @Test
  @DisplayName("deletePost success")
  public void deletePostSuccess() {
    long result = postService.deletePost(1);
    assertEquals(1, result);
  }

  @Test
  @DisplayName("deletePost fail with wrong id and return 0")
  public void deletePostFail() {
    long result = postService.deletePost(0);
    assertEquals(0, result);
  }

  @Test
  @DisplayName("countPostByUserId with valid userId")
  public void countPostByUserIdSuccess() {
    long result = postService.countPostByUserId(1L);
    assertNotEquals(0, result);
  }

  @Test
  @DisplayName("countPostByUserId with invalid userId")
  public void countPostByUserIdSuccessWithInvalidUserID() {
    long result = postService.countPostByUserId(5L);
    assertEquals(0, result);
  }

  @Test
  @DisplayName("findAllPostByUserIdAndPagination success")
  public void findAllPostByUserIdAndPaginationSuccess() {
    List<Post> result = postService.findAllPostByUserIdAndPagination(1, 1);
    assertEquals(5, result.size());
  }

  @Test
  @DisplayName("findAllPostByUserIdAndPagination success")
  public void findAllPostByUserIdAndPaginationSuccessWithPage2() {
    List<Post> result = postService.findAllPostByUserIdAndPagination(1, 2);
    assertEquals(1, result.size());
  }

  @Test
  @DisplayName("findAllPostPagination success")
  public void findAllPostPaginationSuccessPage1() {
    List<Post> result = postService.findAllPostPagination(1);
    assertEquals(5, result.size());
  }

  @Test
  @DisplayName("findAllPostPagination success page 2")
  public void findAllPostPaginationSuccessPage2() {
    List<Post> result = postService.findAllPostPagination(2);
    assertEquals(5, result.size());
  }

  @Test
  @DisplayName("findAllPostPagination return null page 3")
  public void findAllPostPaginationSuccessPage3() {
    List<Post> result = postService.findAllPostPagination(3);
    assertNull(result);
  }

  @Test
  @DisplayName("createPost success")
  public void findPostByTitleAndContentAndTagNameWithUserIdSuccess() {
    Search<Post> result = postService.findPostByTitleAndContentAndTagNameWithUserId("", 1, "a2z", 1);
    verify(postDAO).ListPostByQuery(criteriaQuery, 0, 5);
    assertNotNull(result.getListItems());
    assertEquals(10, result.getTotalItems());
    assertEquals(2, result.getMaxPages());
  }


}
