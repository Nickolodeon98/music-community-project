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

    private boolean isDeleted = Boolean.FALSE;

    public static Notification of(NotificationType notificationType, Member member, Long fromMemberNo, Long recommendNo, Long commentNo) {
        return Notification.builder()
                .notificationType(notificationType)
                .member(member)
                .fromMemberNo(fromMemberNo)
                .recommendNo(recommendNo)
                .commentNo(commentNo)
                .build();
    }
}
