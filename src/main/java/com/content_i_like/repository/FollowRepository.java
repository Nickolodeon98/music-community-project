package com.content_i_like.repository;

import com.content_i_like.domain.entity.Follow;
import com.content_i_like.domain.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

  Long countByMember(Member member);

  Long countByFromMemberNo(Long memberNo);

  Page<Follow> findAllByMember(Member member, Pageable pageable);

  Page<Follow> findAllByFromMemberNo(Long memberNo, Pageable pageable);
}
