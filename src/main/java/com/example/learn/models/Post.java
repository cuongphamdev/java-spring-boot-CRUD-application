package com.example.learn.models;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "Post")
@Table(name = "posts")
public class Post extends AuditModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "content", nullable = false)
  private String content;

  @ManyToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  private User user;

  @Column(name = "user_id", nullable = false)
  private long userId;

  @ManyToMany(mappedBy = "posts", cascade = CascadeType.REMOVE)
  private Set<Tag> tags;

  public Post() {
  }

  public Post(String title, String content, User user) {
    this.title = title;
    this.content = content;
    this.user = user;
  }

  public Post(String title, String content, long userId) {
    this.title = title;
    this.content = content;
    this.userId = userId;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Set<Tag> getTags() {
    return tags;
  }

  public void setTags(Set<Tag> tags) {
    this.tags = tags;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  @Override
  public String toString() {
    return "Post{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", content='" + content + '\'' +
            ", user=" + user +
            '}';
  }
}