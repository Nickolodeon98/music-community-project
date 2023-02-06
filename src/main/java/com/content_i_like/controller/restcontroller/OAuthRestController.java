package com.content_i_like.controller.restcontroller;

import com.content_i_like.domain.dto.oauth.GetSocialOauthRes;
import com.content_i_like.service.OAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
public class OAuthRestController {

  private final OAuthService oAuthService;

  @GetMapping("/{type}")
  public void socialLoginRequest(@PathVariable("type") String type, HttpServletResponse response)
      throws IOException {
    System.out.println("/{type}");
    String requestURL = oAuthService.request(type.toUpperCase());
    System.out.println(requestURL);
    response.sendRedirect(requestURL);
  }

  @GetMapping("/google/redirect")
  public ResponseEntity<?> callbackGoogle(@RequestParam(name = "code") String code)
      throws JsonProcessingException {
    System.out.println("/google/redirect");
    GetSocialOauthRes getSocialOAuthRes = oAuthService.googleOAuthLogin(code);
    return new ResponseEntity<>(getSocialOAuthRes, HttpStatus.OK);
  }

  @GetMapping("/naver/redirect")
  public ResponseEntity<?> callbackNaver(@RequestParam(name = "code") String code,
      @RequestParam(name = "state") String state)
      throws JsonProcessingException, UnsupportedEncodingException {
    System.out.println("/naver/redirect");
    GetSocialOauthRes getSocialOAuthRes = oAuthService.naverOAuthLogin(code, state);
    return new ResponseEntity<>(getSocialOAuthRes, HttpStatus.OK);
  }
}
