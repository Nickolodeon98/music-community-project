package com.content_i_like.service;

import com.content_i_like.domain.dto.member.PointResponse;
import com.content_i_like.domain.entity.Comment;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Point;
import com.content_i_like.domain.enums.PointTypeEnum;
import com.content_i_like.observer.events.notification.CommentNotificationEvent;
import com.content_i_like.observer.events.notification.PointWelcomeNotificationEvent;
import com.content_i_like.repository.CommentRepository;
import com.content_i_like.repository.PointRepository;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {

  private final PointRepository pointRepository;
  private final CommentRepository commentRepository;
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
  public List<PointResponse> pointList(Member member, Pageable pageable) {
    List<Point> points = pointRepository.findByMember(member, pageable);
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

  //출석보상을 하루안에 받은 적이 있는지 확인해야한다
  public boolean getAttendancePoint(Member member) {

    Optional<Point> getPoint = pointRepository
        .findByMemberAndPointType(member, PointTypeEnum.ATTENDANCE_CHECK);

    if (!getPoint.isPresent()) {
      return false;
    }
    LocalDate today = LocalDate.now();
    LocalDateTime standardDate = LocalDateTime
        .of(today.getYear(), today.getMonthValue(), today.getDayOfMonth(), 0, 0, 0);

    return (standardDate.isBefore(getPoint.get().getCreatedAt()));
  }

  @Transactional
  public void giveAttendancePoint(Member member) {

    Point point = Point.builder()
        .pointType(PointTypeEnum.ATTENDANCE_CHECK)
        .member(member)
        .pointExpense(0l)
        .pointIncome(100l)
        .targetCommentNo(0l)
        .targetRecommendNo(0l)
        .build();

    pointRepository.save(point);
  }


  @Transactional
  public void usePoint(Member member, Long commentPoint, PointTypeEnum pointTypeEnum,
      Long targetNo) {
    Point point;
    if (pointTypeEnum.equals(PointTypeEnum.COMMENTS)) {
      point = Point.builder()
          .pointType(pointTypeEnum)
          .member(member)
          .pointIncome(0L)
          .targetCommentNo(targetNo)
          .targetRecommendNo(findRecommendByCommentNo(targetNo))
          .pointExpense(commentPoint)
          .build();
    } else {
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

  public Long findRecommendByCommentNo(Long targetCommentNo) {
    Comment comment = commentRepository.findById(targetCommentNo).get();
    return comment.getRecommend().getRecommendNo();
  }
}
