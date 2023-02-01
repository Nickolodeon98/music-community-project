package com.content_i_like.observer.events;

import com.content_i_like.domain.entity.Comment;
import com.content_i_like.domain.entity.Likes;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Notification;
import com.content_i_like.domain.entity.Recommend;
import com.content_i_like.domain.enums.NotificationType;


public class NotificationEvent { // Spring 4.2 부터는 POJO 로 event 객체 활용 가능(권장)

  private final NotificationType notificationType;
  private final Member receiver;
  private final Long fromMemberNo;
  private final Long recommendNo;
  private final Long commentNo;

  private boolean isDeleted = Boolean.FALSE;

  public NotificationEvent(NotificationType notificationType, Member receiver, Long fromMemberNo,
      Long recommendNo, Long commentNo) {
    this.notificationType = notificationType;
    this.receiver = receiver;
    this.fromMemberNo = fromMemberNo;
    this.recommendNo = recommendNo;
    this.commentNo = commentNo;
  }

  public Notification toEntity() {
    return Notification.of(notificationType, receiver, fromMemberNo, recommendNo, commentNo);
  }

  // 댓글 알림 이벤트
  public static NotificationEvent of(Recommend post, Comment comment) {
    return new NotificationEvent(NotificationType.COMMENTS, post.getMember(),
        comment.getMember().getMemberNo(),
        post.getRecommendNo(), comment.getCommentNo());
  }

  // 좋아요 알림 이벤트
  public static NotificationEvent of(Recommend post, Likes likes) {
    return new NotificationEvent(NotificationType.LIKES, post.getMember(),
        likes.getMember().getMemberNo(), post.getRecommendNo(), null);
  }

  public NotificationType getNotificationType() {
    return notificationType;
  }

  public Member getReceiver() {
    return receiver;
  }

  public Long getFromMemberNo() {
    return fromMemberNo;
  }

  public Long getRecommendNo() {
    return recommendNo;
  }

  public Long getCommentNo() {
    return commentNo;
  }

  public boolean isDeleted() {
    return isDeleted;
  }
}
