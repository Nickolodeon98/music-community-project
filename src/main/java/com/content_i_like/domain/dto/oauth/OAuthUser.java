package com.content_i_like.domain.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OAuthUser {

  public String id;
  public String email;
  public String name;
}
