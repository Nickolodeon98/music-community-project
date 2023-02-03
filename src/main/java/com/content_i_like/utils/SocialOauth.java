package com.content_i_like.utils;

import com.content_i_like.domain.dto.oauth.GoogleOAuthToken;
import com.content_i_like.domain.dto.oauth.GoogleUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

public interface SocialOauth {
  String getOauthRedirectURL();
  ResponseEntity<String> requestAccessToken(String code);

  GoogleOAuthToken getAccessToken(ResponseEntity<String> accessToken)
      throws JsonProcessingException;
  ResponseEntity<String> requestUserInfo(GoogleOAuthToken googleOAuthToken);
  GoogleUser getUserInfo(ResponseEntity<String> userInfoResponse) throws JsonProcessingException;

}
