package com.content_i_like.service;

import com.content_i_like.domain.dto.tracks.TrackResponse;
import com.content_i_like.domain.entity.Song;
import com.content_i_like.domain.enums.TrackEnum;
import com.content_i_like.repository.SongRepository;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackService {

    @Value("${spotify.client.id}")
    private String CLIENT_ID;

    @Value("${spotify.client.secret}")
    private String CLIENT_SECRET;

    private final ObjectMapper objectMapper;
    private final SongRepository songRepository;

    public HttpHeaders headerOf(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setBearerAuth(accessToken);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return httpHeaders;
    }
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

    public List<String> collectAllGenres(String filename) throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get(filename));
        String line = "";

        List<String> genres = new ArrayList<>();

        while ((line = reader.readLine()) != null)
            try {
                genres.add(line);
            } catch (Exception e) {
                log.warn("장르를 가져오는 도중 문제가 발생했습니다.");
            }

        return genres;
    }

    public List<List<String>> findTrackIds(String accessToken) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        String searchUri = TrackEnum.BASE_URL.getValue() + "/search";

        List<String> queries =
                collectAllGenres("C:\\\\LikeLion\\\\final-project\\\\content_i_like\\\\src\\\\main\\\\genres.csv");

        List<List<String>> collectedIds = new ArrayList<>();
        List<String> ids = new ArrayList<>();

        for (Object query : queries) {
            log.info("genre:{}",query);
            for (int offset = 0; offset <= 50; offset += 50) {

                String completeUri = searchUri + "?q='genre:" + query + "'" + "&type=track&limit=50&offset=" + offset;

                // 6,010개 장르의 음악들을 각각 최대 100개씩 가져온다
                ResponseEntity<String> response
                        = restTemplate.exchange(completeUri, HttpMethod.GET, new HttpEntity<>(headerOf(accessToken)), String.class);

                JsonNode tracksSource = objectMapper.readTree(response.getBody());

                for (int i = 0; i < 50; i++) {
                    String hrefContainingId = tracksSource.at("/tracks/items/" + i + "/id").asText();
                    /* https://api.spotify.com/v1/tracks/ 의 길이는 0 ~ 33 까지
                     * 그러므로 substring(34, lastIndex) 를 해야 트랙 아이디 값만 저장할 수 있다.
                     * 3H0XfUU13vsWC6smb9guvG */
//                    ids.add(hrefContainingId.substring(34));
                    ids.add(hrefContainingId);
                }

//                List<String> trackTitles = tracksSource.findValuesAsText("name");

//                log.info("response:{}", response.getBody());
                log.info("ids:{}", ids);
                log.info("size:{}", ids.size());

                List<String> tmpIds = new ArrayList<>(ids);
                ids.clear();

                // 현 예시에서 id 50개씩 총 4묶음이 들어간다
                collectedIds.add(tmpIds);


//                log.info("names:{} , offset:{}", trackTitles, offset);
//                log.info("size:{}, offset:{}", trackTitles.size(), offset);

            }
        }

        return collectedIds;
    }

    public List<String> fetchTracks(String accessToken) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        String trackUri = TrackEnum.BASE_URL.getValue() + "/tracks?ids=";

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headerOf(accessToken));

        String trackTitle = "";

        List<List<String>> trackIds = findTrackIds(accessToken);
        List<String> trackTitles = new ArrayList<>();



        return trackTitles;
    }

    public void createMusicDatabase(List<String> songTitles) {
        for (String songTitle : songTitles) {
            Song singleSongRecord = Song.builder().songTitle(songTitle).build();
            songRepository.save(singleSongRecord);
        }
    }
}
