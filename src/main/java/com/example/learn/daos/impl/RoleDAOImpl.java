package com.example.learn.daos.impl;

import com.example.learn.daos.RoleDAO;
import com.example.learn.models.Role;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDAOImpl extends CrudDAOImpl<Role> implements RoleDAO {
  public RoleDAOImpl() {
    super("Role");
  }
}
