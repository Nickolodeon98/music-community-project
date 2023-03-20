package com.content_i_like.service.validchecks;

import com.content_i_like.domain.dto.member.MemberJoinRequest;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.MemberRepository;
import com.content_i_like.service.validchecks.ValidCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberValidation implements ValidCheck<Member> {
  private final MemberRepository memberRepository;
  @Override
  public Member examine(String toCheck) {
    return memberRepository.findByEmail(toCheck)
        .orElseThrow(() -> new ContentILikeAppException(ErrorCode.NOT_FOUND,
            ErrorCode.NOT_FOUND.getMessage()));
  }

  public void validateDuplicatedMember(MemberJoinRequest memberJoinRequest) {
    memberRepository.findByEmail(memberJoinRequest.getEmail())
        .ifPresent(member -> {
          throw new ContentILikeAppException(ErrorCode.DUPLICATED_MEMBER_NAME,
              ErrorCode.DUPLICATED_MEMBER_NAME.getMessage());
        });

    memberRepository.findByNickName(memberJoinRequest.getNickName())
        .ifPresent(member -> {
          throw new ContentILikeAppException(ErrorCode.DUPLICATED_MEMBER_NAME, "이미 사용 중인 닉네임입니다.");
        });
  }

  public Member validateExistingMemberByEmail(String email) {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new ContentILikeAppException(ErrorCode.MEMBER_NOT_FOUND,
            ErrorCode.MEMBER_NOT_FOUND.getMessage()));
    return member;
  }

  public Member validateExistingMemberByNickName(String nickName) {
    Member member = memberRepository.findByNickName(nickName)
        .orElseThrow(() -> new ContentILikeAppException(ErrorCode.MEMBER_NOT_FOUND,
            ErrorCode.MEMBER_NOT_FOUND.getMessage()));
    return member;
  }
}