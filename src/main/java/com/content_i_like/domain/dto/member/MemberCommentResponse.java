package com.content_i_like.domain.dto.member;

import com.content_i_like.domain.dto.comment.CommentMyFeedResponse;
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
  private String introduction;
  private Long recommendCnt;
  private Long commentCnt;
  private Long followerCnt;
  private Long followingCnt;
  private Page<CommentMyFeedResponse> commentMyFeedResponse;

  public MemberCommentResponse(Member member, Long[] followCnt,
      Page<CommentMyFeedResponse> commentMyFeedResponse) {
    this.email = member.getEmail();
    this.profileImgUrl = member.getProfileImgUrl();
    this.nickName = member.getNickName();
    this.introduction = member.getIntroduction();
    this.recommendCnt = followCnt[0];
    this.commentCnt = followCnt[3];
    this.followerCnt = followCnt[1];
    this.followingCnt = followCnt[2];
    this.commentMyFeedResponse = commentMyFeedResponse;
  }
}
