package com.content_i_like.domain.dto.notification;

import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Notification;
import com.content_i_like.domain.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
    private Long notificationNo;
    private NotificationType notificationType;
    private Long fromMemberNo;
    private Long recommendNo;
    private Long commentNo;

    public static NotificationResponse of(Notification notification) {
        return NotificationResponse.builder()
                .notificationNo(notification.getNotificationNo())
                .notificationType(notification.getNotificationType())
                .fromMemberNo(notification.getFromMemberNo())
                .commentNo(notification.getCommentNo())
                .build();
    }
}