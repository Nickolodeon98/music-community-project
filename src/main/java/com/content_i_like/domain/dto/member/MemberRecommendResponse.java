package com.content_i_like.domain.dto.member;

import com.content_i_like.domain.dto.recommend.RecommendListResponse;
import com.content_i_like.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class MemberRecommendResponse {
  private String email;
  private String profileImgUrl;
  private String nickName;
  private Long recommendCnt;
  private Long followerCnt;
  private Long followingCnt;
  private Page<RecommendListResponse> recommendListResponses;


  public MemberRecommendResponse(Member member, Long recommendCnt, Long followerCnt, Long followingCnt,
      Page<RecommendListResponse> recommendListResponses) {
    this.email = member.getEmail();
    this.profileImgUrl = member.getProfileImgUrl();
    this.nickName = member.getNickName();
    this.recommendCnt = recommendCnt;
    this.followerCnt = followerCnt;
    this.followingCnt = followingCnt;
    this.recommendListResponses = recommendListResponses;
  }
}
