package com.content_i_like.utils;

import com.content_i_like.domain.dto.oauth.NaverOAuthToken;
import com.content_i_like.domain.dto.oauth.NaverUser;
import com.content_i_like.domain.dto.oauth.OAuthToken;
import com.content_i_like.domain.dto.oauth.OAuthUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
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
public class NaverOauth implements SocialOauth {

  @Value(("${spring.security.oauth2.client.provider.naver.authorization-uri}"))
  private String NAVER_SNS_LOGIN_URL;

  @Value("${spring.security.oauth2.client.registration.naver.client-id}")
  private String NAVER_SNS_CLIENT_ID;

  @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
  private String NAVER_SNS_CALLBACK_URL;

  @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
  private String NAVER_SNS_CLIENT_SECRET;

  @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
  private String NAVER_TOKEN_REQUEST_URL;

  @Autowired
  private ObjectMapper objectMapper;

  public String getOauthRedirectURL(String state) {
    Map<String, Object> params = new HashMap<>();
    params.put("response_type", "code");
    params.put("client_id", NAVER_SNS_CLIENT_ID);
    params.put("state", state);
    params.put("redirect_uri", "http://localhost:8080/api/v1/oauth/naver/redirect");

    String parameterString = params.entrySet().stream()
        .map(x -> x.getKey() + "=" + x.getValue())
        .collect(Collectors.joining("&"));
    String redirectURL = NAVER_SNS_LOGIN_URL + "?" + parameterString;
    log.info("redirect-URL={}", redirectURL);
    return redirectURL;
  }

  @Override
  public String getOauthRedirectURL() throws UnsupportedEncodingException {
    Map<String, Object> params = new HashMap<>();
    params.put("response_type", "code");
    params.put("client_id", NAVER_SNS_CLIENT_ID);
    params.put("state", "contentilike");
    params.put("redirect_uri", encodeValue("http://localhost:8080/api/v1/oauth/naver/redirect"));

    String parameterString = params.entrySet().stream()
        .map(x -> x.getKey() + "=" + x.getValue())
        .collect(Collectors.joining("&"));
    String redirectURL = NAVER_SNS_LOGIN_URL + "?" + parameterString;
    log.info("redirect-URL={}", redirectURL);
    return redirectURL;
  }

  @Override
  public ResponseEntity<String> requestAccessToken(String code) {
    RestTemplate restTemplate = new RestTemplate();
    Map<String, Object> params = new HashMap<>();
    System.out.println("state:" + "contentilike");

    params.put("grant_type", "authorization_code");
    params.put("client_id", NAVER_SNS_CLIENT_ID);
    params.put("client_secret", NAVER_SNS_CLIENT_SECRET);
    params.put("code", code);
    params.put("state", "contentilike");

    System.out.println("token-uri: " + NAVER_TOKEN_REQUEST_URL);
    System.out.println("grant_type: "+params.get("grant_type"));
    ResponseEntity<String> stringResponseEntity = restTemplate
        .postForEntity(NAVER_TOKEN_REQUEST_URL, params, String.class);
    System.out.println(stringResponseEntity.getBody()+stringResponseEntity.getHeaders());
    return stringResponseEntity;
  }

  public ResponseEntity<String> requestAccessToken(String code, String state)
      throws UnsupportedEncodingException {
    RestTemplate restTemplate = new RestTemplate();
    Map<String, Object> params = new HashMap<>();
    System.out.println("state:" + state);
    System.out.println("encode state: " + encodeValue(state));
    params.put("grant_type", "authorization_code");
    params.put("client_id", NAVER_SNS_CLIENT_ID);
    params.put("client_secret", NAVER_SNS_CLIENT_SECRET);
    params.put("code", encodeValue(code));
    params.put("state", encodeValue(state));

    System.out.println("token-uri: " + NAVER_TOKEN_REQUEST_URL);
    System.out.println("grant_type: "+params.get("grant_type"));
    System.out.println(NAVER_SNS_CLIENT_ID);
    System.out.println(NAVER_SNS_CLIENT_SECRET);
    ResponseEntity<String> stringResponseEntity = restTemplate
        .postForEntity(NAVER_TOKEN_REQUEST_URL, params, String.class);
    System.out.println(stringResponseEntity.getBody()+stringResponseEntity.getHeaders());
    return stringResponseEntity;
  }

  private String encodeValue(String value) throws UnsupportedEncodingException {
    return URLEncoder.encode(value, "UTF-8");
  }

  @Override
  public NaverOAuthToken getAccessToken(ResponseEntity<String> accessToken)
      throws JsonProcessingException {
    log.info("access token body: {}", accessToken.getBody());
    return objectMapper.readValue(accessToken.getBody(), NaverOAuthToken.class);
  }

  @Override
  public ResponseEntity<String> requestUserInfo(OAuthToken naverOAuthToken) {
    String NAVER_USERINFO_REQUEST_URL = "https://openapi.naver.com/v1/nid/me";
    HttpHeaders headers = new HttpHeaders();
    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
    headers.add("Authorization", "Bearer" + naverOAuthToken.getAccess_token());
    ResponseEntity<String> exchange = restTemplate
        .exchange(NAVER_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);
    System.out.println(exchange.getBody());
    return exchange;
  }

  @Override
  public NaverUser getUserInfo(ResponseEntity<String> userInfoResponse)
      throws JsonProcessingException {
    NaverUser naverUser = objectMapper.readValue(userInfoResponse.getBody(), NaverUser.class);
    return naverUser;
  }
}
