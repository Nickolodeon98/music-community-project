package com.content_i_like.repository;

import com.content_i_like.domain.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmail(String email);

  Optional<Member> findByNickName(String nickName);

  Optional<Member> findBySnsCheck(String snsCheck);

  @Transactional
  @Modifying
  @Query(value = "UPDATE member set password = :pw where member_no= :memberNo", nativeQuery = true)
  void updateMemberPassword(@Param("memberNo") Long memberNo, @Param("pw") String pw);

  Optional<Page<Member>> findByNickNameContaining(String searchKey, Pageable pageable);
}
