package com.example.learn.repositories;

import com.example.learn.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    public List<Comment> findCommentsByPostId(long postId);
    public List<Comment> findCommentsByUserId(long userId);
    @Query(value = "SELECT c FROM Comment c WHERE post_id = :postId AND parent_id = :parentId")
    public List<Comment> findCommentsByPostIdAndAndParentId(@Param("postId") long postId, @Param("parentId") long parentId);

    @Query(value = "SELECT c FROM Comment c WHERE post_id = :postId AND parent_id = null")
    public List<Comment> findRootCommentsListByPostId(@Param("postId") long postId);
}
