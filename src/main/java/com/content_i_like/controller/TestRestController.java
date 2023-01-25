package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.tracks.TrackResponse;
import com.content_i_like.domain.enums.TrackEnum;
import com.content_i_like.service.TrackService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@Slf4j
public class TestRestController {

    private final TrackService trackService;

//    @GetMapping
//    public String redirectPoint() {
//        return "Hello World!";
//    }

    @GetMapping("/tracks")
    public Response<TrackResponse> getTracks() throws JsonProcessingException {
        log.info("hello");
        String uri = "https://api.spotify.com/v1/tracks/{trackId}";
        TrackResponse trackResponse = trackService.fetchTracks(uri);
        return Response.success(trackResponse);
    }

    @GetMapping("/token")
    public ResponseEntity<?> requirePermission() {
        HttpHeaders headers = new HttpHeaders();

        String uri = "https://accounts.spotify.com/authorize?"
                + String.format("client_id=%s&response_type=%s&redirect_uri=%s", TrackEnum.CLIENT_ID.getValue(),
                "code", TrackEnum.REDIRECT_URI.getValue());

        headers.setLocation(URI.create(uri));

        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping("")
    public String getAccessToken(@RequestParam String code) throws JsonProcessingException {

        log.info("code:{}", code);
        String accessToken = trackService.spotifyAccessTokenGenerator(code);

        log.info("accessToken:{}", accessToken);

        return accessToken;
    }
}
