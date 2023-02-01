package com.content_i_like.observer.handler;

import com.content_i_like.domain.entity.Recommend;
import com.content_i_like.observer.events.NotificationEvent;
import com.content_i_like.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventHandler {
  private final NotificationRepository notificationRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @TransactionalEventListener
  public void saveCommentNotification(NotificationEvent event) {
    log.info("알림 생성 type: {}, to: {}, from: {}", event.getNotificationType(),
        event.getReceiver().getMemberNo(),
        event.getFromMemberNo());
    notificationRepository.save(event.toEntity());
  }


}
