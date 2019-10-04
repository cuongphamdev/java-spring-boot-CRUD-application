package com.example.learn.daos;

import com.example.learn.daos.impl.PostDAOImpl;
import com.example.learn.models.Post;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostDAOTest {

  @Mock
  CriteriaQuery<Post> criteria;

  @InjectMocks
  PostDAO postDAO = new PostDAOImpl();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @BeforeEach
  public void setupEach() {
    when(criteria.select(any())).thenReturn(criteria);
    when(criteria.where((Expression<Boolean>) any())).thenReturn(criteria);
  }
}
