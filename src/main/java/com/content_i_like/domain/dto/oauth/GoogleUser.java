package com.content_i_like.domain.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GoogleUser extends OAuthUser {

  public Boolean verifiedEmail;
  public String givenName;
  public String familyName;
  public String picture;
  public String locale;
}
