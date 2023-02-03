package com.content_i_like.domain.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoogleOAuthToken {

  private String access_token;
  private int expires_in;
  private String scope;
  private String token_type;
  private String id_token;

}
