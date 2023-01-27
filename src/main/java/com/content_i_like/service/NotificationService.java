package com.content_i_like.service;

import com.content_i_like.domain.dto.notification.NotificationResponse;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Notification;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.MemberRepository;
import com.content_i_like.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    // To-do: refactor NOT_FOUND -> MEMBER_NOT_FOUND
    public Slice<NotificationResponse> getAllNotifications(String nickName, Pageable pageable) {
        Member member = memberRepository.findByNickName(nickName).orElseThrow(()-> new ContentILikeAppException(ErrorCode.NOT_FOUND, "존재하지 않는 사용자입니다"));

        Slice<Notification> notifications = notificationRepository.findSliceAllByMember(member, pageable);
        return notifications.map(NotificationResponse::of);
    }

}
