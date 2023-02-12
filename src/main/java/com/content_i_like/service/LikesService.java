package com.content_i_like.service;

import com.content_i_like.domain.entity.Likes;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Notification;
import com.content_i_like.domain.entity.Recommend;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.observer.events.notification.CommentNotificationEvent;
import com.content_i_like.observer.events.notification.LikesNotificationEvent;
import com.content_i_like.repository.LikesRepository;
import com.content_i_like.repository.MemberRepository;
import com.content_i_like.repository.NotificationRepository;
import com.content_i_like.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikesService {

  private final RecommendRepository recommendRepository;
  private final MemberRepository memberRepository;
  private final LikesRepository likesRepository;
  private final NotificationRepository notificationRepository;
  private final ApplicationEventPublisher applicationEventPublisher;

  /**
   * 좋아요 상태를 변환합니다.
   *
   * @param userEmail   api를 전송한 user의 email
   * @param recommendNo 추천글 고유 번호
   * @return 결과 문자열
   */
  public String changeLikesStatus(final String userEmail, final Long recommendNo) {
    // 좋아요를 누르는 유저 확인
    Member member = validateGetMemberInfoByUserEmail(userEmail);

    // 좋아요를 누를 추천 게시글 확인
    Recommend post = validateGetRecommendInfoByRecommendNo(recommendNo);

    // 해당 유저가 추천글에 이미 좋아요를 누른 적이 있는지 확인
    Optional<Likes> recordedLikes = likesRepository.findLikesByMemberAndRecommend(member, post);

    // 추천글 작성자가 이미 좋아요 알림을 받았는지 확인
    Optional<Notification> recordedNotification = notificationRepository.findNotificationByMemberAndFromMemberNoAndRecommendNoAndCommentNo(
        post.getMember(),
        member.getMemberNo(), post.getRecommendNo(), null);

    Likes like;
    Notification notification;

    // 좋아요 기록이 있는지 확인합니다.
    if (recordedLikes.isPresent()) {
      // 좋아요 기록이 있다면 정보를 받아옵니다.
      like = recordedLikes.get();

      // 알림 기록이 있다면 정보를 받아옵니다.
//      notification = recordedNotification.get();

    } else {
      like = likesRepository.save(Likes.toEntity(post, member));
      Optional<Likes> checkStatus = likesRepository.findById(like.getLikesNo());

      // 좋아요 알림 이벤트를 발생시킵니다.
      applicationEventPublisher.publishEvent(LikesNotificationEvent.of(post, like));

      return "좋아요를 눌렀습니다";
    }


    // 받아온 like 기록중 getDeletedAt의 정보를 확인합니다.
    if (like.getDeletedAt() == null) {
      // 이미 like를 한 적이 있는데 getDeletedAt이 NULL이라면 다시 한 번 버튼을 누른 것이므로 좋아요를 취소합니다.
      System.out.println("좋아요를 취소 합니다.");
      likesRepository.delete(like);

      // like를 soft delete 처리한 후 알람 기록도 삭제합니다.
//      notificationRepository.delete(notification);

      return "좋아요를 취소했습니다.";
    } else {
      // 이미 like를 한 기록이 있는데 getDeletedAt이 있다면, 좋아요를 취소한 상태에서 다시 좋아요 버튼을 누른 상황입니다.
      // deletedAt 기록을 삭제합니다.
      like.cancelDeletion();
      likesRepository.save(like);
      System.out.println("삭제된 좋아요를 다시 누릅니다.");


      // 다시 알람을 보냅니다.
      applicationEventPublisher.publishEvent(LikesNotificationEvent.of(post, like));

      return "좋아요를 눌렀습니다";
    }
  }

  /**
   * 좋아요 개수를 반환합니다.
   *
   * @param recommendNo 추천글 고유 번호
   * @return 좋아요 개수
   */
  public Long countNumberLikes(Long recommendNo) {
    // 해당 추천글이 존재하는지 확인해야한다.
    return likesRepository.countLikesByRecommend(
        validateGetRecommendInfoByRecommendNo(recommendNo));
  }

  private Recommend validateGetRecommendInfoByRecommendNo(final Long recommendNo) {
    return recommendRepository.findById(recommendNo)
        .orElseThrow(() -> {
          throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
        });
  }

  private Member validateGetMemberInfoByUserEmail(final String userEmail) {
    return memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> {
          throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
        });
  }
}
