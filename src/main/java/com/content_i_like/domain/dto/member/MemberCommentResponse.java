package com.content_i_like.domain.dto.member;

import com.content_i_like.domain.dto.comment.CommentReadResponse;
import com.content_i_like.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MemberCommentResponse {

  private String email;
  private String profileImgUrl;
  private String nickName;
  private Long recommendCnt;
  private Long followerCnt;
  private Long followingCnt;
  private Page<CommentReadResponse> commentReadResponses;

  public MemberCommentResponse(Member member, Long[] followCnt,
      Page<CommentReadResponse> commentReadResponses) {
    this.email = member.getEmail();
    this.profileImgUrl = member.getProfileImgUrl();
    this.nickName = member.getNickName();
    this.recommendCnt = followCnt[0];
    this.followerCnt = followCnt[1];
    this.followingCnt = followCnt[2];
    this.commentReadResponses = commentReadResponses;
  }
}
