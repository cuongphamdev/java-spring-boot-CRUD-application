package com.example.learn.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "Tag")
@Table(name = "tags")
public class Tag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "name", nullable = false)
  private String name;

  @JsonIgnore
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "tag_posts",
          joinColumns = {@JoinColumn(name = "tag_id")},
          inverseJoinColumns = {@JoinColumn(name = "post_id")}
  )
  private Set<Post> posts;


  public Tag() {
  }

  public Tag(String name) {
    this.name = name;
  }

  public Tag(String name, Set<Post> posts) {
    this.name = name;
    this.posts = posts;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Set<Post> getPosts() {
    return posts;
  }

  public void setPosts(Set<Post> posts) {
    this.posts = posts;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
