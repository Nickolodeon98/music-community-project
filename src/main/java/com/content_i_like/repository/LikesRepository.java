package com.content_i_like.repository;

import com.content_i_like.domain.entity.Likes;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

  Optional<Likes> findLikesByMemberAndRecommend(Member member, Recommend post);

  @Query("select count(l) from Likes l where l.deletedAt is null and l.recommend = :recommend")
  Long countLikesByRecommend(@Param("recommend") Recommend recommend);

}
