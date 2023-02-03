package com.content_i_like.observer.events.notification;

import com.content_i_like.domain.entity.Comment;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Recommend;
import com.content_i_like.domain.enums.NotificationType;

public class CommentNotificationEvent extends BaseNotificationEvent {


  public CommentNotificationEvent(NotificationType notificationType,
      Member receiver, Long fromMemberNo, Long recommendNo,
      Long commentNo) {
    super(notificationType, receiver, fromMemberNo, recommendNo, commentNo);
  }

  /**
   * 댓글 알림 이벤트를 생성합니다
   * @param post   추천글
   * @param comment 댓글
   */
  public static CommentNotificationEvent of(Recommend post, Comment comment) {
    return new CommentNotificationEvent(NotificationType.COMMENTS, post.getMember(),
        comment.getMember().getMemberNo(),
        post.getRecommendNo(), comment.getCommentNo());
  }
}
