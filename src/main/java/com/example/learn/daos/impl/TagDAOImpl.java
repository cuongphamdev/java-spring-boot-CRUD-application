package com.example.learn.daos.impl;

import com.example.learn.daos.TagDAO;
import com.example.learn.models.Tag;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class TagDAOImpl implements TagDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Tag> findAll() {
    String hql = "FROM Tag";
    List<Tag> tags = entityManager.createQuery(hql).getResultList();
    return tags;
  }

  @Override
  public Tag findById(long id) {
    try {
      String hql = "FROM Tag WHERE id = :tagId";
      Query query = entityManager.createQuery(hql)
              .setParameter("tagId", id);
      Tag tag = (Tag) query.getSingleResult();
      return tag;
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public long create(Tag tag) {
    try {
      Session session = entityManager.unwrap(Session.class);
      return (long) session.save(tag);
    } catch (Exception e) {
      return 0;
    }
  }

  @Override
  @Transactional
  public long update(Tag tag) {
    try {
      Session session = entityManager.unwrap(Session.class);
      session.update(tag);
      return tag.getId();
    } catch (Exception e) {
      return 0;
    }
  }

  @Override
  @Transactional
  public long delete(Tag tag) {
    try {
      entityManager.remove(tag);
      return tag.getId();
    } catch (Exception e) {
      return 0;
    }
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
