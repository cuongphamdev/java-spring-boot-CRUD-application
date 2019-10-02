package com.example.learn.daos.impl;

import com.example.learn.daos.TagDAO;
import com.example.learn.models.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class TagDAOImpl extends CrudDAOImpl<Tag> implements TagDAO {

  @PersistenceContext
  private EntityManager entityManager;

  public TagDAOImpl() {
    super("Tag");
  }

  @Override
  public List<Tag> searchTag(String queryString) {
    try {
      String hql = "FROM Tag WHERE name LIKE %:name%";
      Query query = entityManager.createQuery(hql);
      query.setParameter("name", queryString).setMaxResults(5);
      return query.getResultList();
    } catch (Exception e) {
      return null;
    }
  }
}
