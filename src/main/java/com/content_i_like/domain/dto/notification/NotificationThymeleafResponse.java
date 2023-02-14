package com.content_i_like.domain.dto.notification;

import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Notification;
import com.content_i_like.domain.entity.Recommend;
import com.content_i_like.domain.enums.NotificationType;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationThymeleafResponse {

  private NotificationType notificationType;
  private String fromMemberNickName;
  private String fromMemberProfileUrl;
  private String fewHoursAgo;
  private String notificationContent;
  private String recommendTitle;

  public static NotificationThymeleafResponse of(Notification notification, Member fromMember, String recommendTitle, String comments) {
    return NotificationThymeleafResponse.builder()
        .notificationType(notification.getNotificationType())
        .fromMemberNickName(fromMember.getNickName())
        .fromMemberProfileUrl(fromMember.getProfileImgUrl())
        .fewHoursAgo(
            Duration.between(notification.getCreatedAt(), LocalDateTime.now()).toHours() + "시간전")
        .notificationContent(comments)
        .recommendTitle(recommendTitle)
        .build();
  }
}