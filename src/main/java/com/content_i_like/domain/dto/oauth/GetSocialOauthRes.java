package com.content_i_like.domain.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetSocialOauthRes {
  private String jwtToken;
  private Long userNum;
  private String accessToken;
  private String tokenType;
}
