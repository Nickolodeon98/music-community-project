package com.content_i_like.service;

import com.content_i_like.config.JwtService;
import com.content_i_like.domain.dto.oauth.GetSocialOauthRes;
import com.content_i_like.domain.dto.oauth.GoogleOAuthToken;
import com.content_i_like.domain.dto.oauth.GoogleUser;
import com.content_i_like.domain.dto.oauth.OAuthToken;
import com.content_i_like.domain.dto.oauth.OAuthUser;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.MemberRepository;
import com.content_i_like.utils.GoogleOauth;
import com.content_i_like.utils.NaverOauth;
import com.content_i_like.utils.SocialOauth;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

  private final GoogleOauth googleOauth;
  private final NaverOauth naverOauth;
  private final JwtService jwtService;
  private final MemberRepository memberRepository;

  public String request(String type) throws UnsupportedEncodingException {
    String redirectURL = "";
    System.out.println(type);
    if (type.equals("GOOGLE")) {
      redirectURL = googleOauth.getOauthRedirectURL();
    } else if (type.equals("NAVER")) {
      redirectURL = naverOauth.getOauthRedirectURL();
    }
    //redirectURL = socialOauth.getOauthRedirectURL();

    return redirectURL;
  }

  public GetSocialOauthRes naverOAuthLogin(String code, String state)
      throws JsonProcessingException, UnsupportedEncodingException {
    System.out.println("naverOAuthLogin메서드");
    ResponseEntity<String> accessToken = naverOauth.requestAccessToken(code, state);
    System.out.println(accessToken.getBody());
    OAuthToken oAuthToken = naverOauth.getAccessToken(accessToken);
    System.out.println(oAuthToken.getAccess_token());

    ResponseEntity<String> userInfoResponse = naverOauth.requestUserInfo(oAuthToken);
    OAuthUser oAuthUser = naverOauth.getUserInfo(userInfoResponse);

    String userEmail = oAuthUser.getEmail();
    System.out.println(userEmail);
    Member member = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new ContentILikeAppException(
            ErrorCode.MEMBER_NOT_FOUND, ErrorCode.MEMBER_NOT_FOUND.getMessage()));
    String jwt = jwtService.generateToken(member);
    return new GetSocialOauthRes(jwt, member.getMemberNo(), oAuthToken.getAccess_token(),
        oAuthToken.getToken_type());
  }

  public GetSocialOauthRes googleOAuthLogin(String code)
      throws JsonProcessingException {
    System.out.println("oauthservice - googleOAuthLogin");
    ResponseEntity<String> accessToken = googleOauth.requestAccessToken(code);
    OAuthToken oAuthToken = googleOauth.getAccessToken(accessToken);

    ResponseEntity<String> userInfoResponse = googleOauth.requestUserInfo(oAuthToken);
    OAuthUser oAuthUser = googleOauth.getUserInfo(userInfoResponse);

    String userEmail = oAuthUser.getEmail();
    System.out.println(userEmail);
    Member member = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new ContentILikeAppException(
            ErrorCode.MEMBER_NOT_FOUND, ErrorCode.MEMBER_NOT_FOUND.getMessage()));
    String jwt = jwtService.generateToken(member);
    return new GetSocialOauthRes(jwt, member.getMemberNo(), oAuthToken.getAccess_token(),
        oAuthToken.getToken_type());
  }
}
