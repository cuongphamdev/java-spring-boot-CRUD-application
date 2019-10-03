package com.example.learn.services.impl;

import com.example.learn.daos.RoleDAO;
import com.example.learn.models.Role;
import com.example.learn.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

  @Autowired
  RoleDAO roleDAO;

  @Override
  public Role createRole(Role role) {
    return roleDAO.create(role);
  }
}
