package com.example.learn.services.impl;

import com.example.learn.daos.PostDAOV2;
import com.example.learn.daos.UserDAO;
import com.example.learn.models.Post;
import com.example.learn.models.Search;
import com.example.learn.models.User;
import com.example.learn.services.PostService;
import com.example.learn.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Service
@Qualifier("PostService2")
public class PostServiceImplV2 implements PostService {

  private final int DEFAULT_PAGE_BREAK = 5;

  @Autowired
  private PostDAOV2 postDAO;

  @Autowired
  private UserDAO userDAO;

  @Override
  public List<Post> findAllPosts() {
    return postDAO.findAll();
  }

  @Override
  public Post findPostById(long postId) {
    return postDAO.findById(postId);
  }

  @Override
  public Post createNewPost(String title, String content, long userId) {
    try {
      User author = userDAO.findById(userId);
      if (author == null) {
        throw new Exception("Author not found with id = [" + userId + "]");
      }
      Post post = new Post(title, content, author.getId());
      return postDAO.create(post);
    } catch (Exception e) {
      return null;
    }
  }

  @Transactional
  @Override
  public Post updatePost(long postId, String title, String content) {
    try {
      Post postFound = postDAO.findById(postId);
      if (postFound == null) throw new Exception("Post not found with id = [" + postId + "]");
      postFound.setTitle(title);
      postFound.setContent(content);
      return postDAO.update(postFound);
    } catch (Exception e) {
      return null;
    }
  }

  @Transactional
  @Override
  public long deletePost(long postId) {
    return postDAO.delete(postId);
  }

  @Override
  public long countPostByUserId(long userId) {
    return postDAO.countPostByUserId(userId);
  }

  @Override
  public List<Post> findAllPostByUserIdAndPagination(int userId, int page) {
    int maxItems = 5;
    int firstPosition = CommonUtils.getFirstResult(page, maxItems);
    return postDAO.findAllPostByUserIdAndPagination(userId, firstPosition, maxItems);
  }

  @Override
  public List<Post> findAllPostPagination(int page) {
    int maxItems = 5;
    int firstPosition = CommonUtils.getFirstResult(page, maxItems);
    return postDAO.findAllPostPagination(firstPosition, maxItems);
  }

  @Override
  public Post createPost(Post post) {
    return postDAO.create(post);


  }

  @Override
  public Search<Post> findPostByTitleAndContentAndTagNameWithUserId(String query, long userId, String order, int page) {
    CriteriaQuery<Post> criteriaQuery = postDAO.getPostSearchTitleNameTagViaUserIdCriteriaQuery(CommonUtils.getSearchString(query), userId, order);
    return searchPost(query, criteriaQuery, page, DEFAULT_PAGE_BREAK);
  }

  private Search<Post> searchPost(String query, CriteriaQuery<Post> criteriaQuery, int page, int pageBreak) {
    try {
      int totalItems = postDAO.countPostsByQuery(criteriaQuery);
      int maxPages = CommonUtils.getMaxPage(totalItems, pageBreak);
      int firstResult = CommonUtils.getFirstResult(page, pageBreak);
      List<Post> postList = postDAO.ListPostByQuery(criteriaQuery, firstResult, pageBreak);
      Search<Post> result = new Search<>(postList, totalItems, maxPages, query, page);
      return result;
    } catch (Exception e) {
      return new Search<>(null, 0, 0, query, page);
    }
  }

  @Override
  public Search<Post> searchPostByTitleAndContentAndNameUserWithSortAndPageBreak(
          String query, String order, int page, int pageBreak, int tagId
  ) {
    try {
      String searchQuery = CommonUtils.getSearchString(query).toLowerCase();
      CriteriaQuery<Post> criteriaQuery = postDAO.searchPostByTitleAndContentAndNameUserWithSortAndPageBreakQuery(query, order, tagId);
      Search<Post> result = searchPost(searchQuery, criteriaQuery, page, pageBreak);
      result.setSortBy(order);
      result.setPageBreak(pageBreak);
      result.setAnotherDataId(tagId);
      return searchPost(query, criteriaQuery, page, pageBreak);
    } catch (Exception e) {
      return new Search<>(null, 0, 0, query, page);
    }
  }

}
