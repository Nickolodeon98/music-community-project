package com.content_i_like.domain.dto.notification;

import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequest {
  private NotificationType notificationType;
  private Long fromMemberNo;
  private Long recommendNo;
  private Long commentNo;

}
