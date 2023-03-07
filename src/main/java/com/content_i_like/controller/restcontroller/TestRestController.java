package com.content_i_like.controller.restcontroller;

import com.content_i_like.domain.enums.TrackEnum;
import com.content_i_like.service.TrackService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@Slf4j
public class TestRestController {

  private final TrackService trackService;

  @Value("${spotify.client.id}")
  private String CLIENT_ID;

  @GetMapping("/new/tracks")
  @ResponseBody
  public String getNewTracks(@RequestParam String token) throws IOException {
    trackService.createAllThreeTypesDBAllUnique(token);

    return "새로운 DB 저장이 완료되었습니다.";
  }

  @GetMapping("/token")
  @ResponseBody
  public ResponseEntity<?> requirePermission(@RequestParam(required = false) String option,
      @RequestParam(required = false) String keyword, HttpServletResponse httpServletResponse) {
    HttpHeaders headers = new HttpHeaders();

    String uri = "https://accounts.spotify.com/authorize?"
        + String.format("client_id=%s&response_type=%s", CLIENT_ID, "code");

    URI redirectPath = URI.create(uri + "&redirect_uri=" + TrackEnum.REDIRECT_URI.getValue());

    if (option.equals("unplanned")) {
      Cookie cookie = new Cookie("keyword", keyword);
      cookie.setMaxAge(3 * 24 * 60 * 60);
      httpServletResponse.addCookie(cookie);

      redirectPath
          = URI.create(uri + "&redirect_uri=" + TrackEnum.SECOND_REDIRECT_URI.getValue());
    }

    headers.setLocation(redirectPath);

    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
  }

  @GetMapping("")
  @ResponseBody
  public ResponseEntity<?> getAccessToken(@RequestParam String code)
      throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();

    String accessToken = trackService.spotifyAccessTokenGenerator(code);

    String uri = "http://localhost:8080/api/v1/test/new/tracks?token=" + accessToken;

    headers.setLocation(URI.create(uri));

    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
  }

  /* 이 때 세션에 저장된 쿠키의 값을 읽어들인다. */
  @GetMapping("/demand")
  @ResponseBody
  public ResponseEntity<?> unPlannedCall(@RequestParam String code,
      @CookieValue(value = "keyword", defaultValue = "love") String keyword)
      throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();

    String accessToken = trackService.spotifyAccessTokenGenerator(code);

    String uri =
        "http://localhost:8080/api/v1/search/tracks/demand?token=" + accessToken + "&keyword=" + keyword;

    headers.setLocation(URI.create(uri));

    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
  }
}