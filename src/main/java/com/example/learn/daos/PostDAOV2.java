package com.example.learn.daos;

import com.example.learn.models.Post;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public interface PostDAOV2 extends CrudDAO<Post> {
  public long countPostByUserId(long userId);

  public List<Post> findAllPostByUserIdAndPagination(int userId, int firstPosition, int maxResults);

  public List<Post> findAllPostPagination(int firstPosition, int maxPages);

  public CriteriaQuery<Post> searchPostByTitleAndContentAndNameUserWithSortAndPageBreakQuery(String postQuery, String order, int tagId);

  public CriteriaQuery<Post> getPostSearchTitleNameTagViaUserIdCriteriaQuery(String query, long userId, String order);

  public List<Post> ListPostByQuery(CriteriaQuery<Post> criteria, int firstResult, int maxResults);

  public int countPostsByQuery(CriteriaQuery<Post> criteria);

  public void orderBy(String field, String type, CriteriaBuilder builder, CriteriaQuery<Post> criteria, Root<Post> root);
}
