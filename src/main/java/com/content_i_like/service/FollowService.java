package com.content_i_like.service;

import com.content_i_like.domain.entity.Follow;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.FollowRepository;
import com.content_i_like.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

  private final FollowRepository followRepository;
  private final MemberRepository memberRepository;

  /**
   * 팔로우가 이미 등록되어 있는지 확인합니다
   *
   * @param fromNickName 팔로우하는 주체의 닉네임
   * @param nickName  팔로우 당하는 상대방의 닉네임
   * @return 팔로우가 되어있지 않다면 null
   */
  public Follow findByMemberAndMemberNo(String fromNickName, String nickName) {
    Member member = memberRepository.findByNickName(nickName)
        .orElseThrow(() -> new ContentILikeAppException(ErrorCode.MEMBER_NOT_FOUND,
            ErrorCode.MEMBER_NOT_FOUND.getMessage()));

    Member fromMember = memberRepository.findByNickName(fromNickName)
        .orElseThrow(() -> new ContentILikeAppException(ErrorCode.MEMBER_NOT_FOUND,
            ErrorCode.MEMBER_NOT_FOUND.getMessage()));

    Optional<Follow> followOptional = followRepository.findByMemberAndFromMemberNo(member,
        fromMember.getMemberNo());

    return followOptional.orElse(null);
  }

  /**
   * 팔로우를 취소합니다
   *
   * @param fromNickName 팔로우 취소하는 주체의 닉네임
   * @param nickName  팔로우 취소하고자 하는 대상의 닉네임
   * @return 팔로윙 목록
   */
  public void followCancel(String fromNickName, String nickName) {
    Follow follow = findByMemberAndMemberNo(fromNickName, nickName);
    followRepository.delete(follow);
  }

  /**
   * 팔로우에 추가합니다
   *
   * @param fromNickName 팔로우 하고자 하는 주체의 닉네임
   * @param nickName  팔로우 하고자 하는 대상의 닉네임
   * @return 팔로워 목록
   */
  public void follow(String fromNickName, String nickName) {
    Member member = memberRepository.findByNickName(nickName)
        .orElseThrow(() -> new ContentILikeAppException(ErrorCode.MEMBER_NOT_FOUND,
            ErrorCode.MEMBER_NOT_FOUND.getMessage()));

    Member fromMember = memberRepository.findByNickName(fromNickName)
        .orElseThrow(() -> new ContentILikeAppException(ErrorCode.MEMBER_NOT_FOUND,
            ErrorCode.MEMBER_NOT_FOUND.getMessage()));

    Follow getFollow = findByMemberAndMemberNo(fromNickName, nickName);
    if (getFollow != null) {
      throw new ContentILikeAppException(ErrorCode.DATABASE_ERROR,
          ErrorCode.DATABASE_ERROR.getMessage());
    }

    Follow follow = Follow.builder()
        .member(member)
        .fromMemberNo(fromMember.getMemberNo())
        .build();
    followRepository.save(follow);
  }
}
