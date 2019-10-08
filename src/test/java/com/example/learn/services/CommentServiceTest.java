package com.example.learn.services;

import com.example.learn.Utils;
import com.example.learn.daos.CommentDAO;
import com.example.learn.models.Comment;
import com.example.learn.services.impl.CommentServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CommentServiceTest {

  private final long WRONG_COMMENT_ID = 0;
  private final long VALID_COMMENT_ID = 1;

  @Mock
  CommentDAO commentDAO;

  @Mock
  @Qualifier("PostService2")
  PostService postService;

  @Mock
  UserService userService;


  @InjectMocks
  CommentService commentService = new CommentServiceImpl();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @BeforeEach
  private void setupEach() {

    when(userService.findUserById(anyLong())).thenAnswer(invocationOnMock ->
    {
      long userId = (Long) invocationOnMock.getArgument(0);
      if (Utils.checkValidBetween(userId, 0, ServiceDataTest.dummyUserList.length)) {
        return ServiceDataTest.dummyUserList[(int) (userId - 1)];
      }
      return null;
    });

    when(postService.findPostById(anyLong())).thenAnswer(invocationOnMock ->
    {
      long postId = (Long) invocationOnMock.getArgument(0);
      if (Utils.checkValidBetween(postId, 0, ServiceDataTest.dummyPostList.length)) {
        return ServiceDataTest.dummyPostList[(int) postId];
      }
      return null;
    });

    when(commentDAO.listRootCommentByPostId(anyLong())).thenAnswer(
            invocationOnMock ->
            {
              long postId = (Long) invocationOnMock.getArgument(0);
              if (Utils.checkValidBetween(postId, 0, ServiceDataTest.dummyPostList.length)) {
                List<Comment> commentList = new ArrayList<>();
                for (Comment comment : ServiceDataTest.dummyCommentList) {
                  if (comment.getPostId() == postId) commentList.add(comment);
                }
                return Arrays.asList(ServiceDataTest.dummyCommentList);
              }
              return null;
            }
    );

    when(commentDAO.findById(anyLong())).thenAnswer(
            invocationOnMock ->
            {
              long commentId = (Long) invocationOnMock.getArgument(0);
              if (Utils.checkValidBetween(commentId, 0, ServiceDataTest.dummyCommentList.length)) {
                return ServiceDataTest.dummyCommentList[(int) (commentId - 1)];
              }
              return null;
            }
    );

    when(commentDAO.create(any())).thenAnswer(
            invocationOnMock ->
            {
              Comment comment = (Comment) invocationOnMock.getArgument(0);
              if (Utils.checkValidBetween(comment.getUserId(), 0, ServiceDataTest.dummyUserList.length) &&
                      Utils.checkValidBetween(comment.getPostId(), 0, ServiceDataTest.dummyPostList.length)) {
                return comment;
              }
              return null;
            }
    );

    when(commentDAO.update(any())).thenAnswer(invocationOnMock ->
    {
      Comment comment = (Comment) invocationOnMock.getArgument(0);
      if (Utils.checkValidBetween(comment.getUserId(), 0, ServiceDataTest.dummyUserList.length) &&
              Utils.checkValidBetween(comment.getPostId(), 0, ServiceDataTest.dummyPostList.length)) {
        return comment;
      }
      return null;
    });

    when(commentDAO.delete(anyLong())).thenAnswer(
            invocationOnMock ->
            {
              long commentId = (Long) invocationOnMock.getArgument(0);
              if (Utils.checkValidBetween(commentId, 0, ServiceDataTest.dummyCommentList.length)) {
                return commentId;
              }
              return (long) 0;
            }
    );

    when(commentDAO.countCommentByUserId(anyLong())).thenAnswer(
            invocationOnMock ->
            {
              long userId = (long) invocationOnMock.getArgument(0);
              if (userId == 0) return (long) 0;
              long count = 0;
              for (Comment comment : ServiceDataTest.dummyCommentList) {
                if (comment.getUserId() == userId) count++;
              }
              return count;
            }
    );

    when(commentDAO.findAll()).thenReturn(Arrays.asList(ServiceDataTest.dummyCommentList));

    when(commentDAO.findCommentByUserId(anyLong())).thenAnswer(
            invocationOnMock ->
            {
              long userId = (Long) invocationOnMock.getArgument(0);
              if (Utils.checkValidBetween(userId, 0, ServiceDataTest.dummyUserList.length)) {
                List<Comment> commentList = new ArrayList<>();
                for (Comment comment : ServiceDataTest.dummyCommentList) {
                  if (comment.getPostId() == userId) commentList.add(comment);
                }
                return Arrays.asList(ServiceDataTest.dummyCommentList);
              }
              return null;
            }
    );
  }


  @DisplayName("findAllRootCommentByPostId success return list comment")
  @Test
  void findAllRootCommentByPostIdSuccess() {
    List<Comment> result = commentService.findAllRootCommentByPostId(VALID_COMMENT_ID);
    assertEquals(Arrays.asList(ServiceDataTest.dummyCommentList).size(), result.size());
  }

  @DisplayName("findAllRootCommentByPostId fail with wrong postId")
  @Test
  void findAllRootCommentByPostIdFail() {
    List<Comment> result = commentService.findAllRootCommentByPostId(WRONG_COMMENT_ID);
    assertEquals(null, result);
  }

  @DisplayName("findCommentById success ")
  @Test
  void findCommentByIdSuccess() {
    Comment result = commentService.findCommentById(1);
    assertEquals(ServiceDataTest.dummyCommentList[0].getContent(), result.getContent());
  }

  @DisplayName("findCommentById fail ")
  @Test
  void findCommentByIdFail() {
    Comment result = commentService.findCommentById(WRONG_COMMENT_ID);
    assertEquals(null, result);
  }

  @DisplayName("createComment success ")
  @Test
  void createCommentSuccess() {
    Comment inputComment = new Comment("content", 1, 1, 0);
    Comment result = commentService.createComment(inputComment.getContent(), inputComment.getPostId(), inputComment.getUserId(), inputComment.getParentId());
    verify(commentDAO).create(argThat(createdComment -> {
      return createdComment.getContent().equals(inputComment.getContent()) &&
              createdComment.getPostId() == inputComment.getPostId() &&
              createdComment.getUserId() == inputComment.getUserId() &&
              createdComment.getParentId() == null;
    }));
    assertNotNull(result);
  }

  @DisplayName("createComment with parentId success ")
  @Test
  void createCommentSuccessWithParentId() {
    Comment inputComment = new Comment("content", 1, 1, 1);
    Comment result = commentService.createComment(inputComment.getContent(), inputComment.getPostId(), inputComment.getUserId(), inputComment.getParentId());
    verify(commentDAO).create(argThat(createdComment -> {
      return createdComment.getContent().equals(inputComment.getContent()) &&
              createdComment.getPostId() == inputComment.getPostId() &&
              createdComment.getUserId() == inputComment.getUserId() &&
              createdComment.getParentId() == inputComment.getParentId();
    }));
    assertNotNull(result);
  }

  @DisplayName("createComment fail with wrong userId")
  @Test
  void createCommentFailWithWrongUserId() {
    Comment result = commentService.createComment("content", 1, ServiceDataTest.dummyUserList.length, 0);
    assertNull(result);
  }

  @DisplayName("createComment fail with wrong postId")
  @Test
  void createCommentFailWithWrongPostId() {
    Comment result = commentService.createComment("content", ServiceDataTest.dummyPostList.length, 1, 0);
    assertNull(result);
  }

  @DisplayName("updateComment success with valid id")
  @Test
  void updateCommentSuccess() {
    Comment result = commentService.updateComment(1, "new content");
    verify(commentDAO).update(argThat(createdComment -> {
      return createdComment.getContent().equals("new content");
    }));
    assertEquals("new content", result.getContent());
  }

  @DisplayName("updateComment fail with out of list comment range id")
  @Test
  public void updateCommentFail() {
    Comment result = commentService.updateComment(ServiceDataTest.dummyCommentList.length, "content");
    assertNull(result);
  }

  @DisplayName("deleteComment success return id of comment")
  @Test
  public void deleteCommentSuccess() {
    long result = commentService.deleteComment(1);
    verify(commentDAO).delete(1);
    assertEquals(1, result);
  }

  @DisplayName("deleteComment fail return 0 when input comment id out of range of list comment")
  @Test
  public void deleteCommentFail() {
    long result = commentService.deleteComment(ServiceDataTest.dummyCommentList.length);
    assertEquals(0, result);
    verify(commentDAO).delete(ServiceDataTest.dummyCommentList.length);
  }

  @DisplayName("countCommentByUserId success ")
  @Test
  public void countCommentByUserIdSuccess() {
    long input = 1;
    long result = commentService.countCommentByUserId(input);
    int count = 0;
    for (Comment comment : ServiceDataTest.dummyCommentList) {
      if (comment.getUserId() == 1) count++;
    }
    assertEquals(count, result);
    verify(commentDAO).countCommentByUserId(input);
  }

  @DisplayName("countCommentByUserId success with a invalid userId or user don't have any comment ")
  @Test
  public void countCommentByUserIdFailWithInvalidUserId() {
    long input = 0;
    long result = commentService.countCommentByUserId(input);
    verify(commentDAO).countCommentByUserId(input);
    assertEquals(0, result);
  }

  @DisplayName("findCommentsByUserId success return a comment")
  @Test
  public void findCommentsByUserIdSuccess() {
    long input = 1;
    List<Comment> result = commentService.findCommentsByUserId(input);
    verify(commentDAO).findCommentByUserId(input);
    assertNotNull(result);
  }

  @DisplayName("findCommentsByUserId fail return null")
  @Test
  public void findCommentsByUserIdFail() {
    long input = 0;
    List<Comment> result = commentService.findCommentsByUserId(input);
    verify(commentDAO).findCommentByUserId(input);
    assertNull(result);
  }

  @DisplayName("findAllComment success")
  @Test
  public void findAllCommentSuccess() {
    List<Comment> result = commentService.findAllComment();
    assertEquals(Arrays.asList(ServiceDataTest.dummyCommentList), result);
  }
}
