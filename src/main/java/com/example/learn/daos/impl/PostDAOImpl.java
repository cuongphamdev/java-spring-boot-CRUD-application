package com.example.learn.daos.impl;

import com.example.learn.daos.PostDAO;
import com.example.learn.models.Post;
import com.example.learn.models.Search;
import com.example.learn.utils.CommonUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class PostDAOImpl extends CrudDAOImpl<Post> implements PostDAO {

  @PersistenceContext
  private EntityManager entityManager;

  public PostDAOImpl() {
    super("Post");
  }

  @Override
  public long countPostByUserId(long userId) {
    try {
      String hql = "SELECT COUNT (p) FROM Post p WHERE userId = :userId";
      Query query = entityManager.createQuery(hql);
      query.setParameter("userId", userId);
      return (long) query.getSingleResult();
    } catch (Exception e) {
      return 0;
    }
  }

  @Override
  public List<Post> findAllPostByUserId(long userId) {
    try {
      String hql = "FROM Post p WHERE userId = :userId";
      Query query = entityManager.createQuery(hql);
      query.setParameter("userId", userId);
      return query.getResultList();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public List<Post> findAllPostByUserIdAndPagination(int userId, int page) {
    try {
      String hql = "FROM Post p WHERE userId = :userId";
      Query query = entityManager.createQuery(hql);
      query.setParameter("userId", (long) userId);
      query.setFirstResult((page - 1) * 1);
      query.setMaxResults(5);
      return query.getResultList();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public List<Post> findAllPostPagination(int page) {
    try {
      String hql = "FROM Post ORDER BY id DESC";
      Query query = entityManager.createQuery(hql);
      query.setFirstResult((page - 1) * 5);
      query.setMaxResults(5);
      return query.getResultList();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public List<Post> findPostByTitleAndContent(String queryString) {
    queryString = "%" + queryString.toLowerCase() + "%";
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Post> criteria = builder.createQuery(Post.class);
    Root<Post> root = criteria.from(Post.class);
    criteria.select(root);
    criteria.where(builder.or(builder.like(root.get("title"), queryString), builder.like(root.get("content"), queryString)));
    return entityManager.createQuery(criteria).setMaxResults(5).getResultList();
  }

  @Override
  public List<Post> findPostByTitleAndContentAndTagName(String query) {
    query = "%" + query.toLowerCase() + "%";
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Post> criteria = builder.createQuery(Post.class);
    Root<Post> root = criteria.from(Post.class);
    criteria.select(root);
    criteria.where(
            builder.or(
                    builder.like(builder.lower(root.join("tags").get("name")), query),
                    builder.like(builder.lower(root.get("title")), query),
                    builder.like(builder.lower(root.get("content")), query)

            )
    );
    return entityManager.createQuery(criteria).setMaxResults(5).getResultList();
  }

  @Override
  public Search<Post> searchPostByTitleAndContentAndTagNameWithUserId(String query, long userId, String order, int page) {
    String searchQuery = "%" + query.toLowerCase() + "%";
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Post> criteria = builder.createQuery(Post.class);
    Root<Post> root = criteria.from(Post.class);
    criteria.select(root);
    criteria.where(
            builder.and(builder.or(
                    builder.like(builder.lower(root.join("tags", JoinType.LEFT).get("name")), searchQuery),
                    builder.like(builder.lower(root.get("title")), searchQuery),
                    builder.like(builder.lower(root.get("content")), searchQuery))
            ),
            builder.equal(root.get("userId"), userId)
    );
    if (order.equals("a2z")) {
      criteria.orderBy(builder.asc(root.get("title")));
    } else if (order.equals("z2a")) {
      criteria.orderBy(builder.desc(root.get("title")));
    }
    int countItems = entityManager.createQuery(criteria).getResultList().size();
    List<Post> postList = entityManager.createQuery(criteria).setFirstResult((page - 1) * 5).setMaxResults(5).getResultList();
    int maxPages = countItems / 5 + (countItems % 5 != 0 ? 1 : 0);
    Search<Post> result = new Search<Post>(postList, countItems, maxPages, query, page);
    return result;
  }

  @Override
  public Search<Post> searchPostByTitleAndContentAndNameUserWithSortAndPageBreak(String postQuery, String order, int page, int pageBreak, int tagId) {
    String searchPostQuery = CommonUtils.getSearchString(postQuery);
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Post> criteria = builder.createQuery(Post.class);
    Root<Post> root = criteria.from(Post.class);
    criteria.select(root);
    Expression<Boolean> checkTagIdQuery = tagId > 0 ?
            builder.equal(root.join("tags", JoinType.LEFT).get("id"), tagId) :
            builder.greaterThan(root.get("id"), tagId);
    criteria.where(
      builder.and(
              builder.or(
                      builder.like(builder.lower(root.join("user").get("name")), searchPostQuery),
                      builder.like(builder.lower(root.get("title")), searchPostQuery),
                      builder.like(builder.lower(root.get("content")), searchPostQuery)
              ),
              checkTagIdQuery
      )
    );

    switch (order) {
      case "a2z": {
        criteria.orderBy(builder.asc(root.get("title")));
        break;
      }
      case "z2a": {
        criteria.orderBy(builder.desc(root.get("title")));
        break;
      }
      case "commentDESC": {
        Join<Object, Object> comments = root.join("comments", JoinType.LEFT);
        criteria.groupBy(root.get("id"));
        criteria.orderBy(
                builder.desc(builder.count(comments))
        );
        break;
      }
      case "latestPost": {
        criteria.orderBy(builder.desc(root.get("createdAt")));
        break;
      }
    }

    int countItems = entityManager.createQuery(criteria).getResultList().size();
    List<Post> postList = entityManager.createQuery(criteria).setFirstResult((page - 1) * pageBreak).setMaxResults(pageBreak).getResultList();
    int maxPages = countItems / pageBreak + (countItems % pageBreak != 0 ? 1 : 0);
    Search<Post> result = new Search<Post>(postList, countItems, maxPages, postQuery, page);
    result.setSortBy(order);
    result.setPageBreak(pageBreak);
    result.setAnotherDataId(tagId);
    return result;
  }
}
