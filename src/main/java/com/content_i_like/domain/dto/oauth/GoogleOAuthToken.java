package com.content_i_like.domain.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoogleOAuthToken extends OAuthToken {

  private String scope;
  private String id_token;

}
