package com.content_i_like.repository;

import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Point;
import com.content_i_like.domain.enums.PointTypeEnum;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PointRepository extends JpaRepository<Point, Long> {

  List<Point> findByMember(Member member);
  List<Point> findByMember(Member member, Pageable pageable);

  @Transactional
  @Query("select p from Point p where p.member= :memberNo and p.pointType= :pointType order by p.createdAt desc limit 1")
  Optional<Point> findByMemberAndPointType(@Param("memberNo") Member memberNo,
      @Param("pointType") PointTypeEnum pointTypeEnum);
}
