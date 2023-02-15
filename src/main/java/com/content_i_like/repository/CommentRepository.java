package com.content_i_like.repository;

import com.content_i_like.domain.dto.comment.CommentReadResponse;
import com.content_i_like.domain.entity.Comment;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Recommend;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  @Modifying(clearAutomatically = true)
  @Query("update Comment c set c.commentContent = :content, c.lastModifiedAt = current_timestamp where c.commentNo = :commentNo ")
  void update(@Param("content") String commentContent, @Param("commentNo") Long commentNo);

  Optional<Comment> findCommentByRecommend_RecommendNoAndCommentNo(Long recommendNo,
      Long commentNo);

  Page<Comment> findAllByRecommendRecommendNo(Long recommendNo, Pageable pageable);

  Page<Comment> findAllByMember(Member member, Pageable pageable);

  Page<Comment> findCommentsByCommentPointGreaterThanAndRecommendRecommendNo(Long point,
      Long recommendNo,
      Pageable pageable);

  Comment findCommentsByCommentPointGreaterThanAndRecommendRecommendNoAndCommentNo(Long point,
      Long recommendNo,
      Long commentNo);

  Long countByMember(Member member);

  List<Comment> findAllByRecommendRecommendNo(Long recommendNo);

  Optional<List<Comment>> findCommentsByRecommend_RecommendNoAndMember_MemberNo(Long recommendNo, Long memberNo);

}
