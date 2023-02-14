package com.content_i_like.service;

import com.content_i_like.domain.dto.notification.NotificationRequest;
import com.content_i_like.domain.dto.notification.NotificationResponse;
import com.content_i_like.domain.dto.notification.NotificationThymeleafResponse;
import com.content_i_like.domain.entity.Comment;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Notification;
import com.content_i_like.domain.entity.Recommend;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.CommentRepository;
import com.content_i_like.repository.MemberRepository;
import com.content_i_like.repository.NotificationRepository;
import com.content_i_like.repository.RecommendRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final MemberRepository memberRepository;
  private final RecommendRepository recommendRepository;
  private final CommentRepository commentRepository;

  // To-do: refactor NOT_FOUND -> MEMBER_NOT_FOUND
  public Slice<NotificationResponse> getAllNotifications(String nickName, Pageable pageable) {
    Member member = memberRepository.findByNickName(nickName)
        .orElseThrow(() -> new ContentILikeAppException(ErrorCode.NOT_FOUND, "존재하지 않는 사용자입니다"));

    Slice<Notification> notifications = notificationRepository.findSliceAllByMember(member,
        pageable);
    return notifications.map(NotificationResponse::of);
  }

  public List<NotificationThymeleafResponse> getNotificationsThymeleafResponses(String nickName,
      Pageable pageable) {
    Member member = memberRepository.findByNickName(nickName)
        .orElseThrow(() -> new ContentILikeAppException(ErrorCode.NOT_FOUND, "존재하지 않는 사용자입니다"));

    Slice<Notification> notifications = notificationRepository.findSliceAllByMember(member,
        pageable);

    return getListBySlice(notifications);
  }

  public List<NotificationThymeleafResponse> getListBySlice(Slice<Notification> notifications) {
    List<NotificationThymeleafResponse> responseList = new ArrayList<>();

    Member nullMember = Member.builder()
        .nickName("")
        .profileImgUrl("https://content-i-like.s3.ap-northeast-2.amazonaws.com/80c4f0a7-c4e0-44a5-85d6-315dc793fe28-profile.jpg")
        .build();

    for (Notification notification : notifications) {
      String commentContent="";
      String recommendTitle="";
      Member fromMember = nullMember;

      if (notification.getCommentNo() != null) {
        Comment comment = commentRepository.findById(notification.getCommentNo()).orElse(null);
        if (comment != null) {
          commentContent = comment.getCommentContent();
        }
      }

      if (notification.getRecommendNo() != null) {
        Recommend recommend = recommendRepository.findById(notification.getRecommendNo())
            .orElse(null);
        if (recommend != null) {
          recommendTitle = recommend.getRecommendTitle();
        }
      }

      if (notification.getFromMemberNo() != null) {
        fromMember = memberRepository.findById(notification.getFromMemberNo()).orElse(nullMember);
      }

      NotificationThymeleafResponse thymeleafResponse = NotificationThymeleafResponse.of(notification, fromMember,
          recommendTitle, commentContent);
      responseList.add(thymeleafResponse);
    }
    return responseList;
  }

  public NotificationResponse addNotification(NotificationRequest notificationRequest) {
    Optional<Member> byId = memberRepository.findById(14L);
    Notification notification = Notification.builder()
        .notificationType(notificationRequest.getNotificationType())
        .member(byId.get())
        .fromMemberNo(notificationRequest.getFromMemberNo())
        .recommendNo(notificationRequest.getRecommendNo())
        .commentNo(notificationRequest.getCommentNo())
        .build();
    notificationRepository.save(notification);
    return NotificationResponse.of(notification);
  }
}
