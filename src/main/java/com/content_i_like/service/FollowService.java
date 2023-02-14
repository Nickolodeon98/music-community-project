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

  public void followCancel(String fromNickName, String nickName) {
    Follow follow = findByMemberAndMemberNo(fromNickName, nickName);
    followRepository.delete(follow);
  }

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
