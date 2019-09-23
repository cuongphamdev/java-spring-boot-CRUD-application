package com.example.learn.models;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "Role")
@Table(name = "roles")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "name", nullable = false)
  private String name;

  @OneToMany(mappedBy = "role", cascade = CascadeType.REMOVE)
  private Set<User> users;

  public Role() {
  }

  public Role(String name) {
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }
}

