package com.content_i_like.service;

import com.content_i_like.domain.dto.member.PointResponse;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Point;
import com.content_i_like.domain.enums.PointTypeEnum;
import com.content_i_like.observer.events.notification.CommentNotificationEvent;
import com.content_i_like.observer.events.notification.PointWelcomeNotificationEvent;
import com.content_i_like.repository.PointRepository;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {

  private final PointRepository pointRepository;
  private final ApplicationEventPublisher applicationEventPublisher;

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

  //포인트 내역
  public List<PointResponse> pointList(Member member) {
    List<Point> points = pointRepository.findByMember(member);
    return points.stream().map(point -> point.toResponse()).collect(Collectors.toList());
  }

  @Transactional
  public void giveWelcomePoint(Member member) {
    Point point = Point.builder()
        .pointType(PointTypeEnum.WELCOME_POINT)
        .member(member)
        .pointExpense(0l)
        .pointIncome(1000l)
        .targetCommentNo(0l)
        .targetRecommendNo(0l)
        .build();

    pointRepository.save(point);

    // 웰컴 포인트 이벤트 발생시킵니다.
    applicationEventPublisher.publishEvent(PointWelcomeNotificationEvent.of(point));
  }

  @Transactional
  public void usePoint(Member member, Long commentPoint, PointTypeEnum pointTypeEnum, Long targetNo) {
    Point point;
    if (pointTypeEnum.equals(PointTypeEnum.WELCOME_POINT)) {
      point = Point.builder()
          .pointType(pointTypeEnum)
          .member(member)
          .pointIncome(0L)
          .targetCommentNo(targetNo)
          .targetRecommendNo(0l)
          .pointExpense(commentPoint)
          .build();
    }else{
      point = Point.builder()
          .pointType(pointTypeEnum)
          .member(member)
          .pointIncome(0L)
          .targetCommentNo(0L)
          .targetRecommendNo(targetNo)
          .pointExpense(commentPoint)
          .build();
    }

    pointRepository.save(point);
  }
}
