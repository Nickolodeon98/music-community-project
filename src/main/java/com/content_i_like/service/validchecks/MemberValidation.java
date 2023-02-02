package com.content_i_like.service.validchecks;

import com.content_i_like.domain.entity.Member;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.MemberRepository;
import com.content_i_like.service.validchecks.ValidCheck;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberValidation implements ValidCheck<Member> {
  private final MemberRepository memberRepository;
  @Override
  public Member examine(String toCheck) {
    return memberRepository.findByEmail(toCheck)
        .orElseThrow(() -> new ContentILikeAppException(ErrorCode.NOT_FOUND,
            ErrorCode.NOT_FOUND.getMessage()));
  }
}