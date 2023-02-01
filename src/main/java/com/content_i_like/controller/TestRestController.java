package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.domain.dto.tracks.TrackResponse;
import com.content_i_like.domain.enums.TrackEnum;
import com.content_i_like.service.AlbumFetch;
import com.content_i_like.service.ArtistFetch;
import com.content_i_like.service.TrackFetch;
import com.content_i_like.service.TrackService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@Slf4j
public class TestRestController {

  private final TrackService trackService;

  @Value("${spotify.client.id}")
  private String CLIENT_ID;

//    @GetMapping
//    public String redirectPoint() {
//        return "Hello World!";
//    }

  @GetMapping("/tracks")
  public String getTracks(@RequestParam String token) throws IOException {
    trackService.createAllThreeTypesDB(token);

    return "DB 저장이 완료되었습니다.";
  }

  @GetMapping("/token")
  public ResponseEntity<?> requirePermission() {
    HttpHeaders headers = new HttpHeaders();

    String uri = "https://accounts.spotify.com/authorize?"
            + String.format("client_id=%s&response_type=%s&redirect_uri=%s", CLIENT_ID,
            "code", TrackEnum.REDIRECT_URI.getValue());

    headers.setLocation(URI.create(uri));

    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
  }

  @GetMapping("")
  public ResponseEntity<?> getAccessToken(@RequestParam String code)
          throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();

    String accessToken = trackService.spotifyAccessTokenGenerator(code);

    String uri = "http://localhost:8080/api/v1/test/tracks?token=" + accessToken;

    headers.setLocation(URI.create(uri));

    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
  }


}