package com.content_i_like.domain.dto.member;

import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.enums.MemberStatusEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserProfile {

  private final String oauthId;
  private final String name;
  private final String email;
  private final String DEFAULT_PROFILE = "https://content-i-like.s3.ap-northeast-2.amazonaws.com/80c4f0a7-c4e0-44a5-85d6-315dc793fe28-profile.jpg";

  public Member toMember() {
    return Member.builder()
        .email(this.email)
        .name(this.name)
        .snsCheck(this.oauthId)
        .nickName(this.email)
        .status(MemberStatusEnum.USER)
        .profileImgUrl(DEFAULT_PROFILE)
        .build();
  }
}
