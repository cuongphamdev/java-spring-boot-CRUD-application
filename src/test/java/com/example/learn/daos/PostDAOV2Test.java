package com.example.learn.daos;

import com.example.learn.daos.impl.PostDAOImplV2;
import com.example.learn.models.Post;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostDAOV2Test {

  @PersistenceContext(unitName = "Post")
  protected EntityManager entityManager;

  @Spy
  protected CriteriaBuilder builder = entityManager.getCriteriaBuilder();

  @Spy
  protected CriteriaQuery<Post> criteria = builder.createQuery(Post.class);

  @Spy
  protected Root<Post> root = criteria.from(Post.class);

  @InjectMocks
  protected PostDAOV2 postDAO = new PostDAOImplV2();

  @Before
  public void before () {
    when(entityManager.getCriteriaBuilder()).thenReturn(builder);
    when(builder.createQuery(Post.class)).thenReturn(criteria);
    when(criteria.from(Post.class)).thenReturn(root);
    when(criteria.select(root)).thenReturn(criteria);
  }

  @Test
  @DisplayName("getPostSearchTitleNameTagViaUserIdCriteriaQuery with order a2z")
  public void getPostSearchTitleNameTagViaUserIdCriteriaQueryWithOrderA2A () {
    postDAO.getPostSearchTitleNameTagViaUserIdCriteriaQuery("%%", 1, "a2z");

    verify(postDAO).orderBy("title", "asc", builder, criteria, root);
  }
}
