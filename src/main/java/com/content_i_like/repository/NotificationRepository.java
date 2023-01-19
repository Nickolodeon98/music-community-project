package com.content_i_like.repository;

import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findAllByMember(Member member, Pageable pageable);

    List<Notification> findAllByMember(Member member);
}
