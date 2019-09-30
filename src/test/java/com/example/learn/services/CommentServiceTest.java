package com.example.learn.services;

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
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CommentServiceTest {

  @Mock
  CommentDAO commentDAO;

  @InjectMocks
  CommentService commentService = new CommentServiceImpl();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @BeforeEach
  private void setupEach() {
    when(commentDAO.listRootCommentByPostId(anyLong())).thenAnswer(
            invocationOnMock ->
            {
              long postId = (Long) invocationOnMock.getArgument(0);
              if (postId == 0) {
                return null;
              }
              return Arrays.asList(ServiceDataTest.dummyCommentList);
            }
    );

    when(commentDAO.findById(anyLong())).thenAnswer(
            invocationOnMock ->
            {
              long commentId = (Long) invocationOnMock.getArgument(0);
              if (commentId == 0) {
                return null;
              }
              return ServiceDataTest.dummyComment;
            }
    );

    when(commentDAO.findById(anyLong())).thenAnswer(
            invocationOnMock ->
            {
              long commentId = (long) invocationOnMock.getArgument(0);
              if (commentId == 0) {
                return null;
              }
              return ServiceDataTest.dummyComment;
            }
    );

    when(commentDAO.create(any())).thenAnswer(
            invocationOnMock ->
            {
              Comment comment = (Comment) invocationOnMock.getArgument(0);
              if (comment.getUserId() == 0 || comment.getPostId() == 0) {
                return null;
              }
              return ServiceDataTest.dummyComment;
            }
    );

    when(commentDAO.update(any())).thenReturn(ServiceDataTest.dummyComment);

    when(commentDAO.delete(anyLong())).thenAnswer(
            invocationOnMock ->
            {
              long commentId = (Long) invocationOnMock.getArgument(0);
              return commentId;
            }
    );

    when(commentDAO.countCommentByUserId(anyLong())).thenAnswer(
            invocationOnMock ->
            {
              long userId = (Long) invocationOnMock.getArgument(0);
              if (userId == 0) return 0;
              return ServiceDataTest.dummyCommentList.length;
            }
    );

  }

  @DisplayName("findAllRootCommentByPostId success return list comment")
  @Test
  void findAllRootCommentByPostIdSuccess () {
    List<Comment> result = commentService.findAllRootCommentByPostId(1);
    assertEquals(Arrays.asList(ServiceDataTest.dummyCommentList) , result);
  }

  @DisplayName("findAllRootCommentByPostId fail with wrong postId")
  @Test
  void findAllRootCommentByPostIdFail () {
    List<Comment> result = commentService.findAllRootCommentByPostId(0);
    assertEquals( null, result);
  }

  @DisplayName("findCommentById success ")
  @Test
  void findCommentByIdSuccess () {
    Comment result = commentService.findCommentById(1);
    assertEquals( ServiceDataTest.dummyComment, result);
  }

  @DisplayName("findCommentById fail ")
  @Test
  void findCommentByIdFail () {
    Comment result = commentService.findCommentById(0);
    assertEquals( null, result);
  }

  @DisplayName("createComment success ")
  @Test
  void createCommentSuccess () {
    Comment result = commentService.createComment("content", 1, 1, 0);
    assertEquals( ServiceDataTest.dummyComment, result);
  }

  @DisplayName("createComment with parentId success ")
  @Test
  void createCommentSuccessWithParentId () {
    Comment result = commentService.createComment("content", 1, 1, 1);
    assertEquals( ServiceDataTest.dummyComment, result);
  }

  @DisplayName("createComment fail ")
  @Test
  void createCommentFail () {
    Comment result = commentService.createComment("content", 1, 0, 0);
    assertEquals( null, result);
  }

  @DisplayName("updateComment success ")
  @Test
  private void updateCommentSuccess () {
    Comment result = commentService.updateComment(1, "new content");
    assertEquals( ServiceDataTest.dummyComment, result);
  }

  @DisplayName("updateComment fail ")
  @Test
  private void updateCommentFail () {
    Comment result = commentService.updateComment(0, "");
    assertEquals( null, result);
  }

  @DisplayName("deleteComment success ")
  @Test
  private void deleteCommentSuccess () {
    commentService.deleteComment(1);
    verify(commentDAO).delete(1);
  }

  @DisplayName("countCommentByUserId success ")
  @Test
  private void countCommentByUserIdSuccess () {
    long result = commentService.countCommentByUserId(1);
    assertEquals( ServiceDataTest.dummyCommentList.length, result);
    verify(commentDAO).countCommentByUserId(1);
  }

  @DisplayName("countCommentByUserId fail ")
  @Test
  private void countCommentByUserIdFail () {
    long result = commentService.countCommentByUserId(0);
    assertEquals( 0, result);
    verify(commentDAO).countCommentByUserId(0);
  }
}
