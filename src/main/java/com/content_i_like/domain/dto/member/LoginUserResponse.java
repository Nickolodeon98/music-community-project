package com.content_i_like.domain.dto.member;

import com.content_i_like.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class LoginUserResponse {
  private Long memberNo;
  private String memberNickname;
  private String profileImgUrl;

  public static LoginUserResponse of(Member member) {
    return LoginUserResponse
        .builder()
        .memberNo(member.getMemberNo())
        .memberNickname(member.getNickName())
        .profileImgUrl(member.getProfileImgUrl())
        .build();
  }
}
