package com.content_i_like.domain.entity;

import com.content_i_like.domain.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
public class Notification extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long notificationNo;

  @Enumerated(EnumType.STRING)
  private NotificationType notificationType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_no")
  private Member member;

  private Long fromMemberNo;

  private Long recommendNo;
  private Long commentNo;
}
