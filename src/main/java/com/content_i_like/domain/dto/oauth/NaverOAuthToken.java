package com.content_i_like.domain.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NaverOAuthToken extends OAuthToken {

  private int refresh_token;
  private String error;
  private String error_description;
}
