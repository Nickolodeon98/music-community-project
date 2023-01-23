package com.content_i_like.repository;

import com.content_i_like.domain.entity.Likes;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findLikesByMemberAndRecommend(Member member, Recommend post);

    Integer countLikesByRecommend(Recommend recommend);

}
