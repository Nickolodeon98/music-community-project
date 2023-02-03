package com.content_i_like.observer.events.notification;

import com.content_i_like.domain.entity.Likes;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Recommend;
import com.content_i_like.domain.enums.NotificationType;

public class LikesNotificationEvent extends BaseNotificationEvent{

  public LikesNotificationEvent(NotificationType notificationType,
      Member receiver, Long fromMemberNo, Long recommendNo,
      Long commentNo) {
    super(notificationType, receiver, fromMemberNo, recommendNo, commentNo);
  }

  /**
   * 좋아요 알림 이벤트를 생성합니다
   * @param post   추천글
   * @param likes 좋아요
   */
  public static LikesNotificationEvent of(Recommend post, Likes likes) {
    return new LikesNotificationEvent(NotificationType.LIKES, post.getMember(),
        likes.getMember().getMemberNo(), post.getRecommendNo(), null);
  }
}
