package com.example.learn.daos.impl;

import com.example.learn.daos.PostDAOV2;
import com.example.learn.models.Post;
import com.example.learn.utils.CommonUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class PostDAOImplV2 extends CrudDAOImpl<Post> implements PostDAOV2 {

  @PersistenceContext
  private EntityManager entityManager;

  public PostDAOImplV2() {
    super("Post");
  }

  @Override
  public long countPostByUserId(long userId) {
    String hql = "SELECT COUNT (p) FROM Post p WHERE userId = :userId";
    Query query = entityManager.createQuery(hql);
    query.setParameter("userId", userId);
    return (long) query.getSingleResult();
  }

  @Override
  public List<Post> findAllPostByUserIdAndPagination(int userId, int firstPosition, int maxResults) {
    try {
      String hql = "FROM Post p WHERE userId = :userId";
      Query query = entityManager.createQuery(hql);
      query.setParameter("userId", (long) userId);
      query.setFirstResult(firstPosition);
      query.setMaxResults(maxResults);
      return query.getResultList();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public List<Post> findAllPostPagination(int firstPosition, int maxPages) {
    try {
      String hql = "FROM Post ORDER BY id DESC";
      Query query = entityManager.createQuery(hql);
      query.setFirstResult(firstPosition);
      query.setMaxResults(maxPages);
      List<Post> result = query.getResultList();
      return result;
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public List<Post> ListPostByQuery(CriteriaQuery<Post> criteria, int firstResult, int maxResults) {
    return entityManager.createQuery(criteria).setFirstResult(firstResult).
            setMaxResults(maxResults).getResultList();
  }

  @Override
  public int countPostsByQuery(CriteriaQuery<Post> criteria) {
    return entityManager.createQuery(criteria).getResultList().size();
  }

  private Predicate getPredicateLike(String query, String field, CriteriaBuilder builder, Root<Post> root) {
    return builder.like(builder.lower(root.get(field)), query);
  }

  private Predicate getPredicateLikeJoinTag(String query, String field, String tableName, CriteriaBuilder builder, Root<Post> root) {
    return builder.like(builder.lower(root.join(tableName, JoinType.LEFT).get(field)), query);
  }

  public void orderBy(String field, String type, CriteriaBuilder builder, CriteriaQuery<Post> criteria, Root<Post> root) {
    if (type.equals("asc")) {
      criteria.orderBy(builder.asc(root.get(field)));
    } else {
      criteria.orderBy(builder.desc(root.get(field)));
    }
  }

  @Override
  public CriteriaQuery<Post> getPostSearchTitleNameTagViaUserIdCriteriaQuery(String query, long userId, String order) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Post> criteria = builder.createQuery(Post.class);
    Root<Post> root = criteria.from(Post.class);
    String queryString = CommonUtils.getSearchString(query);
    criteria.select(root);
    criteria.where(
            builder.and(builder.or(
                    this.getPredicateLike(queryString, "title", builder, root),
                    this.getPredicateLike(queryString, "content", builder, root),
                    this.getPredicateLikeJoinTag(queryString, "name", "tags", builder, root)
                    ),
                    builder.equal(root.get("userId"), userId)
            )
    );
    if (order.equals("a2z")) {
      criteria.groupBy(root.get("id"));
      orderBy("title", "asc", builder, criteria, root);
    } else if (order.equals("z2a")) {
      criteria.groupBy(root.get("id"));
      orderBy("title", "desc", builder, criteria, root);
    }
    return criteria;
  }

  @Override
  public CriteriaQuery<Post> searchPostByTitleAndContentAndNameUserWithSortAndPageBreakQuery(String query, String order, int tagId) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Post> criteria = builder.createQuery(Post.class);
    Root<Post> root = criteria.from(Post.class);
    criteria.select(root);
    String queryString = "%" + query.toLowerCase() + "%";

    Expression<Boolean> checkTagIdQuery = tagId > 0 ?
            builder.equal(root.join("tags", JoinType.LEFT).get("id"), tagId) :
            builder.greaterThan(root.get("id"), tagId);
    criteria.where(
            builder.and(
                    builder.or(
                            this.getPredicateLike(queryString, "title", builder, root),
                            this.getPredicateLike(queryString, "content", builder, root),
                            this.getPredicateLikeJoinTag(queryString, "name", "user", builder, root)
                    ),
                    checkTagIdQuery
            )
    );

    switch (order) {
      case "a2z": {
        criteria.orderBy(
                sortQuery(builder, criteria, root, "asc", "title")
        );
        break;
      }
      case "z2a": {
        criteria.orderBy(
                sortQuery(builder, criteria, root, "desc", "title")
        );
        break;
      }
      case "commentDESC": {
        Join<Object, Object> comments = root.
                join("comments", JoinType.LEFT);
        criteria.groupBy(root.get("id"));
        criteria.orderBy(
                builder.desc(builder.count(comments))
        );
        break;
      }
      case "latestPost": {
        criteria.orderBy(
                sortQuery(builder, criteria, root, "desc", "createdAt")
        );
        break;
      }
    }
    return criteria;
  }

  private Order sortQuery(CriteriaBuilder builder, CriteriaQuery<Post> criteria, Root<Post> root, String type, String fieldName) {
    criteria.groupBy(root.get("id"));
    if (type.equals("asc")) {
      return builder.asc(root.get(fieldName));
    }
    return builder.desc(root.get(fieldName));

  }

}
