package com.content_i_like.service;

import com.content_i_like.domain.dto.tracks.TrackResponse;
import com.content_i_like.domain.enums.TrackEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpHead;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackService {

    @Value("${spotify.client.id}")
    private String CLIENT_ID;

    @Value("${spotify.client.secret}")
    private String CLIENT_SECRET;

    private final ObjectMapper objectMapper;


//    private final RestTemplate restTemplate;

//    public String grantAuthorizationFromSpotify() {
//        RestTemplate template = new RestTemplate();
//        String uri = "https://accounts.spotify.com/authorize?"
//                + String.format("client_id=%s&response_type=%s&redirect_uri=%s", TrackEnum.CLIENT_ID.getValue(),
//                "code", TrackEnum.REDIRECT_URI.getValue());
//        log.info("uri:{}",uri);
//
//        ResponseEntity<String> response = template.getForEntity(uri, String.class);
//
//        String responseDetails = response.getBody();
//        log.info("response:{}", responseDetails);
//
////        for (Object o : ) {
////            responseDetails
////        }
//        return responseDetails;
//    }

    public String spotifyAccessTokenGenerator(String code) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

//        String code = grantAuthorizationFromSpotify();
//        log.info("code:{}",code);

//        String uri = "https://accounts.spotify.com/api/token";
        MultiValueMap<String, String> requiredRequestBody = new LinkedMultiValueMap<>();
        requiredRequestBody.add("grant_type", TrackEnum.GRANT_TYPE.getValue());
        requiredRequestBody.add("code", code);
        requiredRequestBody.add("redirect_uri", TrackEnum.REDIRECT_URI.getValue());

        HttpHeaders httpHeaders = new HttpHeaders();

        String toEncode = CLIENT_ID + ":" + CLIENT_SECRET;

        String authorization
                = Base64.getEncoder().encodeToString(toEncode.getBytes());

        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth(authorization);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requiredRequestBody, httpHeaders);

        ResponseEntity<String> response = restTemplate.postForEntity(TrackEnum.TOKEN_URL.getValue(), httpEntity, String.class);


        JsonNode tokenSource = objectMapper.readTree(response.getBody());

//        log.info("response:{}", response.getBody());

        String accessToken = String.valueOf(tokenSource.findValue("access_token"));
        String refreshToken = String.valueOf(tokenSource.findValue("refresh_token"));

        return accessToken.substring(1, accessToken.length()-1);
    }



    public TrackResponse fetchTracks(String accessToken, String trackId) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String trackUri = TrackEnum.BASE_URL.getValue() + "/tracks?ids="
                + trackId;

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setBearerAuth(accessToken);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(trackUri, HttpMethod.GET, httpEntity, String.class);

        log.info("tracksInfo:{}",response.getBody());

        JsonNode trackInfoRoot = objectMapper.readTree(response.getBody());

        String trackTitle = String.valueOf(trackInfoRoot.findValue("name"));

        return TrackResponse.builder().title(trackTitle).build();
    }
}
