package com.content_i_like.controller;

import com.content_i_like.domain.dto.oauth.GetSocialOauthRes;
import com.content_i_like.service.CustomOAuth2UserService;
import com.content_i_like.service.OAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class OAuthController {

  private final OAuthService oAuthService;

  @GetMapping("/{type}")
  public void socialLoginRequest(@PathVariable("type") String type, HttpServletResponse response)
      throws IOException {
    String requestURL = oAuthService.request(type.toUpperCase());
    response.sendRedirect(requestURL);
  }

  @GetMapping("/{type}/redirect")
  public ResponseEntity<?> callback(@PathVariable(name = "type") String type,
      @RequestParam(name = "code") String code) throws JsonProcessingException {
    GetSocialOauthRes getSocialOAuthRes = oAuthService.oAuthLogin(code);
    return new ResponseEntity<>(getSocialOAuthRes, HttpStatus.OK);
  }
}
