package com.content_i_like.repository;

import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findPageAllByMember(Member member, Pageable pageable);

    Slice<Notification> findSliceAllByMember(Member member, Pageable pageable);
}
