package com.content_i_like.repository;

import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Point;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {

  List<Point> findByMember(Member member);
}
