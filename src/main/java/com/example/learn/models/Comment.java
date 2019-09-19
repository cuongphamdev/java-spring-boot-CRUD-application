package com.example.learn.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "comments")
public class Comment{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    private Post post;

    @Column (name = "post_id", nullable = false)
    private long postId;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private Comment parent;

    @Column (name = "parent_id", nullable = false, columnDefinition = "int8 default 0")
    private long parentId;

    @OneToMany(mappedBy="parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Comment> childComments;

    public Comment () {}

    public Comment(String content, long postId, long userId, long parentId) {
        this.content = content;
        this.userId = userId;
        this.postId = postId;
        this.parentId = parentId;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Set<Comment> getChildComments() {
        return childComments;
    }

    public void setChildComments(Set<Comment> childComments) {
        this.childComments = childComments;
    }
}
