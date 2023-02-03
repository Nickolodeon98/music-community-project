package com.content_i_like.service;

import com.content_i_like.config.JwtService;
import com.content_i_like.domain.dto.oauth.GetSocialOauthRes;
import com.content_i_like.domain.dto.oauth.GoogleOAuthToken;
import com.content_i_like.domain.dto.oauth.GoogleUser;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.MemberRepository;
import com.content_i_like.utils.SocialOauth;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

  private final SocialOauth socialOauth;
  private final JwtService jwtService;
  private final MemberRepository memberRepository;

  public String request(String type) {
    String redirectURL;
    redirectURL = socialOauth.getOauthRedirectURL();
    return redirectURL;
  }

  public GetSocialOauthRes oAuthLogin(String code) throws JsonProcessingException {
    ResponseEntity<String> accessToken = socialOauth.requestAccessToken(code);
    GoogleOAuthToken googleOAuthToken = socialOauth.getAccessToken(accessToken);

    ResponseEntity<String> userInfoResponse = socialOauth.requestUserInfo(googleOAuthToken);
    GoogleUser googleUser = socialOauth.getUserInfo(userInfoResponse);

    String userEmail = googleUser.getEmail();
    System.out.println(userEmail);
    Member member = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new ContentILikeAppException(
            ErrorCode.MEMBER_NOT_FOUND, ErrorCode.MEMBER_NOT_FOUND.getMessage()));
    String jwt = jwtService.generateToken(member);
    return new GetSocialOauthRes(jwt, member.getMemberNo(), googleOAuthToken.getAccess_token(), googleOAuthToken.getToken_type());
  }

}
