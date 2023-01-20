package com.content_i_like.repository;

import com.content_i_like.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Modifying(clearAutomatically = true)
    @Query("update Comment c set c.commentContent = :content, c.lastModifiedAt = current_timestamp where c.commentNo = :commentNo ")
    void update(@Param("content") String commentContent,@Param("commentNo") Long commentNo);
}
