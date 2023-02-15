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
  private final String DEFAULT_PROFILE = "https://content-i-like.s3.ap-northeast-2.amazonaws.com/c63fc89a-8b4c-4567-8c3d-801125237977-profile.jpg";

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
