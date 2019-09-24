package com.example.learn.utils;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class HibernateUtil {

  @PersistenceContext
  private static EntityManager entityManager;


  public static Session getSession() {
    return entityManager.unwrap(Session.class);
  }
}
