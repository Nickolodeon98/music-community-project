package com.content_i_like.domain.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class MemberLoginResponse {

  private String jwt;
  private Long memberNo;
  private String nickName;
  private String profileImgUrl;
}
