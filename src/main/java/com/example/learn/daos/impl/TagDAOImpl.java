package com.example.learn.daos.impl;

import com.example.learn.daos.TagDAO;
import com.example.learn.models.Tag;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

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
      return (Tag) query.getSingleResult();
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
  public long update(Tag tag) {
    try {
      String hql = "UPDATE Tag SET name = :name " +
              "WHERE id = :tagId";
      Query query = entityManager.createQuery(hql);
      query.setParameter("name", tag.getName())
              .setParameter("tagId", tag.getId());
      return query.executeUpdate();
    } catch (Exception e) {
      return 0;
    }
  }

  @Override
  public long delete(Tag tag) {
    try {
      entityManager.remove(tag);
      return tag.getId();
    } catch (Exception e) {
      return 0;
    }
  }
}
