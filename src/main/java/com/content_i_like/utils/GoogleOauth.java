package com.content_i_like.utils;

import com.content_i_like.domain.dto.oauth.GoogleOAuthToken;
import com.content_i_like.domain.dto.oauth.GoogleUser;
import com.content_i_like.domain.dto.oauth.OAuthToken;
import com.content_i_like.domain.dto.oauth.OAuthUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleOauth implements SocialOauth {

  @Value(("${spring.security.oauth2.client.registration.google.url}"))
  private String GOOGLE_SNS_LOGIN_URL;

  @Value("${spring.security.oauth2.client.registration.google.client-id}")
  private String GOOGLE_SNS_CLIENT_ID;

  @Value("${spring.security.oauth2.client.registration.google.redirect-url}")
  private String GOOGLE_SNS_CALLBACK_URL;

  @Value("${spring.security.oauth2.client.registration.google.client-secret}")
  private String GOOGLE_SNS_CLIENT_SECRET;

  private String GOOGLE_DATA_ACCESS_SCOPE = "https://www.googleapis.com/auth/userinfo.email%20https://www.googleapis.com/auth/userinfo.profile";

  //String 값을 객체로 바꾸는 mapper
  @Autowired
  private ObjectMapper objectMapper;

  //Google API로 요청을 보내고 받을 객체
//  @Autowired
//  private RestTemplate restTemplate;

  //@Override
  public String getOauthRedirectURL() {
    Map<String, Object> params = new HashMap<>();

    params.put("scope", GOOGLE_DATA_ACCESS_SCOPE);
    params.put("response_type", "code");
    params.put("client_id", GOOGLE_SNS_CLIENT_ID);
    params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);

    System.out.println("googleOauth getOauthRedirectURL()");
    System.out.println(GOOGLE_SNS_LOGIN_URL);
    System.out.println(GOOGLE_DATA_ACCESS_SCOPE);
    System.out.println(GOOGLE_SNS_CLIENT_ID);
    System.out.println(GOOGLE_SNS_CALLBACK_URL);
    String parameterString = params.entrySet().stream()
        .map(x -> x.getKey() + "=" + x.getValue())
        .collect(Collectors.joining("&"));
    String redirectURL = GOOGLE_SNS_LOGIN_URL + "?" + parameterString;
    log.info("redirect-URL={}", redirectURL);
    return redirectURL;
  }

  @Override
  public ResponseEntity<String> requestAccessToken(String code) {
    String GOOGLE_TOKEN_REQUEST_URL = "https://oauth2.googleapis.com/token";
    System.out.println("googleOauth - requestAccessToken");
    RestTemplate restTemplate = new RestTemplate();
    Map<String, Object> params = new HashMap<>();
    params.put("code", code);
    params.put("client_id", GOOGLE_SNS_CLIENT_ID);
    params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
    params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
    params.put("grant_type", "authorization_code");
    ResponseEntity<String> stringResponseEntity = restTemplate
        .postForEntity(GOOGLE_TOKEN_REQUEST_URL, params, String.class);

    return stringResponseEntity;
  }

  @Override
  public GoogleOAuthToken getAccessToken(ResponseEntity<String> accessToken)
      throws JsonProcessingException {
    log.info("access token body: {}", accessToken.getBody());
    return objectMapper.readValue(accessToken.getBody(), GoogleOAuthToken.class);
  }

  @Override
  public ResponseEntity<String> requestUserInfo(OAuthToken googleOAuthToken) {
    String GOOGLE_USERINFO_REQUEST_URL = "https://www.googleapis.com/oauth2/v2/userinfo";
    System.out.println("googleOauth - requestUserInfo");
    HttpHeaders headers = new HttpHeaders();
    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
    headers.add("Authorization", "Bearer" + googleOAuthToken.getAccess_token());
    ResponseEntity<String> exchange = restTemplate
        .exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);
    System.out.println(exchange.getBody());
    return exchange;
  }

  @Override
  public GoogleUser getUserInfo(ResponseEntity<String> userInfoResponse)
      throws JsonProcessingException {
    GoogleUser googleUser = objectMapper.readValue(userInfoResponse.getBody(), GoogleUser.class);
    return googleUser;
  }


}
