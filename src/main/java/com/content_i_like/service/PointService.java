package com.content_i_like.service;

import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Point;
import com.content_i_like.domain.enums.PointTypeEnum;
import com.content_i_like.repository.PointRepository;
import java.util.Iterator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {

  private final PointRepository pointRepository;

  /* 주의: income과 expense는 null이어서는 안된다 */

  //멤버별 포인트 계산하기
  public Long calculatePoint(Member member) {
    Long point = 0l;
    List<Point> points = pointRepository.findByMember(member);

    Iterator<Point> iterPoint = points.iterator();
    while (iterPoint.hasNext()) {
      Point po = iterPoint.next();
      point += po.getPointIncome() - po.getPointExpense();
    }

    return point;
  }

  @Transactional
  public void giveWelcomePoint(Member member) {
    Point point = Point.builder()
        .pointType(PointTypeEnum.WELCOME_POINT)
        .member(member)
        .pointExpense(0l)
        .pointIncome(1000l)
        .build();

    pointRepository.save(point);
  }
}
