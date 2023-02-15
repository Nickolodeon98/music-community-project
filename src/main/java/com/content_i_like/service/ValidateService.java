package com.content_i_like.service;

import com.content_i_like.domain.entity.Comment;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.CommentRepository;
import com.content_i_like.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateService {

  private final MemberRepository memberRepository;
  private final CommentRepository commentRepository;

  public Member validateMemberByMemberNo(Long memberNo) {
    return memberRepository.findById(memberNo)
        .orElseThrow(() -> {
          throw new ContentILikeAppException(ErrorCode.MEMBER_NOT_FOUND,
              ErrorCode.MEMBER_NOT_FOUND.getMessage());
        });
  }

  public Comment validateGetCommentInfoByCommentNo(final Long commentNo) {
    return commentRepository.findById(commentNo)
        .orElseThrow(() -> {
          throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
        });
  }
}
