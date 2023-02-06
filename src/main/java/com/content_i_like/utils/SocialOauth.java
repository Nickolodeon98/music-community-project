package com.content_i_like.utils;

import com.content_i_like.domain.dto.oauth.OAuthToken;
import com.content_i_like.domain.dto.oauth.OAuthUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.UnsupportedEncodingException;
import org.springframework.http.ResponseEntity;

public interface SocialOauth {
  String getOauthRedirectURL() throws UnsupportedEncodingException;
  ResponseEntity<String> requestAccessToken(String code);

  OAuthToken getAccessToken(ResponseEntity<String> accessToken)
      throws JsonProcessingException;
  ResponseEntity<String> requestUserInfo(OAuthToken oAuthToken);
  OAuthUser getUserInfo(ResponseEntity<String> userInfoResponse) throws JsonProcessingException;

}
