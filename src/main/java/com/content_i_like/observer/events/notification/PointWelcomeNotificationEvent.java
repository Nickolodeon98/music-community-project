package com.content_i_like.observer.events.notification;

import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Point;
import com.content_i_like.domain.enums.NotificationType;

public class PointWelcomeNotificationEvent extends BaseNotificationEvent {

  public PointWelcomeNotificationEvent(
      NotificationType notificationType,
      Member receiver, Long fromMemberNo, Long recommendNo,
      Long commentNo) {
    super(notificationType, receiver, fromMemberNo, recommendNo, commentNo);
  }

  /**
   * 웰컴 포인트 알림 이벤트를 생성합니다
   * @param point   포인트
   */
  public static PointWelcomeNotificationEvent of(Point point) {
    return new PointWelcomeNotificationEvent(NotificationType.POINT_WELCOME, point.getMember(), null, null,
        null);
  }
}
