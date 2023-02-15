package com.content_i_like.service;

import com.content_i_like.domain.entity.Album;
import com.content_i_like.domain.entity.Artist;
import com.content_i_like.domain.entity.NewAlbum;
import com.content_i_like.domain.entity.NewArtist;
import com.content_i_like.domain.entity.NewTrack;
import com.content_i_like.domain.entity.Track;
import com.content_i_like.domain.enums.TrackEnum;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.AlbumRepository;
import com.content_i_like.repository.ArtistRepository;
import com.content_i_like.repository.NewAlbumRepository;
import com.content_i_like.repository.NewArtistRepository;
import com.content_i_like.repository.NewTrackRepository;
import com.content_i_like.repository.TrackRepository;
import com.content_i_like.service.fetchoptions.AlbumFetch;
import com.content_i_like.service.fetchoptions.ArtistFetch;
import com.content_i_like.service.fetchoptions.Fetch;
import com.content_i_like.service.fetchoptions.NewAlbumFetch;
import com.content_i_like.service.fetchoptions.NewArtistFetch;
import com.content_i_like.service.fetchoptions.NewFetch;
import com.content_i_like.service.fetchoptions.NewTrackFetch;
import com.content_i_like.service.fetchoptions.TrackFetch;
import com.content_i_like.service.saveoptions.AlbumSave;
import com.content_i_like.service.saveoptions.ArtistSave;
import com.content_i_like.service.saveoptions.DBSaveOption;
import com.content_i_like.service.saveoptions.NewAlbumSave;
import com.content_i_like.service.saveoptions.NewArtistSave;
import com.content_i_like.service.saveoptions.NewDBSaveOption;
import com.content_i_like.service.saveoptions.NewTrackSave;
import com.content_i_like.service.saveoptions.TrackSave;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
@Slf4j
public class NewTrackService {

  @Value("${spotify.client.id}")
  private String CLIENT_ID;

  @Value("${spotify.client.secret}")
  private String CLIENT_SECRET;

  private final ObjectMapper objectMapper;
  private final NewTrackRepository trackRepository;
  private final NewArtistRepository artistRepository;
  private final NewAlbumRepository albumRepository;

  public HttpHeaders headerOf(String accessToken) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setBearerAuth(accessToken);
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    return httpHeaders;
  }

  public String spotifyAccessTokenGenerator(String code) throws JsonProcessingException {
    RestTemplate restTemplate = new RestTemplate();

    /* String uri = "https://accounts.spotify.com/api/token"; */
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

    HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requiredRequestBody,
        httpHeaders);

    ResponseEntity<String> response = restTemplate.postForEntity(TrackEnum.TOKEN_URL.getValue(),
        httpEntity, String.class);

    JsonNode tokenSource = objectMapper.readTree(response.getBody());

    String accessToken = String.valueOf(tokenSource.findValue("access_token"));
    String refreshToken = String.valueOf(tokenSource.findValue("refresh_token"));

    return accessToken.substring(1, accessToken.length() - 1);
  }

  public List<String> collectAllGenres(String filename) throws IOException {
    BufferedReader reader = Files.newBufferedReader(Paths.get(filename));
    List<String> genres = new ArrayList<>();
    String line = "";

    while ((line = reader.readLine()) != null) {
      try {
        genres.add(line);
      } catch (Exception e) {
        log.warn("장르를 가져오는 도중 문제가 발생했습니다.");
      }
    }
    return genres;
  }

  @Transactional
  public List<List<String>> findSpotifyIds(String accessToken) throws IOException {
    RestTemplate restTemplate = new RestTemplate();
    String searchUri = TrackEnum.BASE_URL.getValue() + "/search";
    int limit = 50;

//    List<String> queries =
//        collectAllGenres(
//            "C:\\\\LikeLion\\\\final-project\\\\content_i_like\\\\src\\\\main\\\\k-genres.csv");

//    List<String> queries = List.of("Korean Mask Singer", "Korean Traditional", "Korean Phantom Singer", "Korean Instrumental");

    List<String> queries = List.of("K-pop", "K-indie", "K-rap", "K-rock", "Classic K-pop", "Korean Soundtrack", "Korean OST",
        "Korean Pop", "Pop", "Korean Instrumental", "K-jazz", "K-R&B");

//    , "Pop ballads", "Pop", "J-pop", "J-rock", "J-poprock", ""

    List<List<String>> collectedIds = new ArrayList<>();
    List<String> ids = new ArrayList<>();

    for (Object query : queries) {
      log.info("genre:{}", query);
      for (int offset = 0; offset <= 950; offset += 50) {
        String completeUri =
            searchUri + "?q=genre:" + query + "&type=track&limit=" + limit + "&offset=" + offset
                + "&market=KR";
        // 72개 장르의 음악들을 각각 최대 150개씩 가져온다
        ResponseEntity<String> response
            = restTemplate.exchange(completeUri, HttpMethod.GET,
            new HttpEntity<>(headerOf(accessToken)), String.class);

        JsonNode tracksSource = objectMapper.readTree(response.getBody());

        if (tracksSource.at("/tracks/items").isEmpty()) {
          log.info("해당 장르에 속한 음원이 없습니다!");
          continue;
        }
        for (int i = 0; i < 50; i++) {
          String hrefContainingId = tracksSource.at("/tracks/items/" + i + "/id").asText();

          ids.add(hrefContainingId);
        }
        log.info("ids:{}", ids);
        log.info("size:{}", ids.size());

        List<String> tmpIds = new ArrayList<>(ids);
        ids.clear();

        // 현 예시에서 id 50개씩 묶음들이 들어간다
        collectedIds.add(tmpIds);
      }
    }
    return collectedIds;
  }

  public List<JsonNode> callTracksApi(String accessToken) throws IOException {
    List<JsonNode> collectedJsonNodes = new ArrayList<>();
    RestTemplate restTemplate = new RestTemplate();
    String trackUri = TrackEnum.BASE_URL.getValue() + "/tracks?ids=";

    List<List<String>> spotifyIds = findSpotifyIds(accessToken);

    for (List<String> spotifyId : spotifyIds) {
      StringBuilder ids = new StringBuilder();
      for (int i = 0; i < spotifyId.size(); i++) {
        ids.append(spotifyId.get(i));
        if (i != 49) {
          ids.append(",");
        }
      }
//      log.info("ids:{}", ids);
      HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(
          headerOf(accessToken));

      ResponseEntity<String> response = restTemplate
          .exchange(trackUri + ids, HttpMethod.GET, httpEntity, String.class);

      /* track uri 이용해서 50개씩 모아져 있는 아이디들로 찾아지는 음원들에 대한 JSON 형태 응답을 모두 읽어들이고,
       * 읽어들인 응답에서 필요한 부분만 추출한다. 추출은 매개 변수로 받은 인터페이스의 구현체에 따라 달라진다. */
      JsonNode infoRoot = objectMapper.readTree(response.getBody());

//      log.info("info:{}", infoRoot.get("tracks").get(0).get("album").get("images").get(1).get("url").asText());

      collectedJsonNodes.add(infoRoot);
    }
    return collectedJsonNodes;
  }

  public <T> Set<T> fetchTracksWithoutDuplicates(List<JsonNode> infoRoots,
      NewFetch<T> fetchedType) {
    Set<String> titles = new HashSet<>();
    String data = "";
    for (JsonNode infoRoot : infoRoots) {
      for (int j = 0; j < 50; j++) {
        data = fetchedType.extractData(infoRoot, j);
        if (data.equals("")) {
          continue;
        }
        titles.add(data);
      }
    }
    return fetchedType.parseIntoEntitiesAllUnique(titles);
  }

  @Transactional
  public <T> void createMusicDatabaseAllUnique(Set<T> entities, NewDBSaveOption<T> saveOption) {
    saveOption.saveNewRowsAllUnique(entities);
  }

  @Transactional
  public void createAllThreeTypesDBAllUnique(String token) throws IOException {
    /* TODO: 세 자원을 모두 저장을 할 때 여기도 템플릿 콜백 패턴 적용 가능 */
    List<JsonNode> jsonData = callTracksApi(token);

    Set<NewArtist> artistEntities = fetchTracksWithoutDuplicates(jsonData, new NewArtistFetch());
    createMusicDatabaseAllUnique(artistEntities, new NewArtistSave(artistRepository));

    Set<NewAlbum> albumEntities = fetchTracksWithoutDuplicates(jsonData, new NewAlbumFetch());

    // 파싱을 나중에 앨범 다 저장하고 다시 다른 방식으로 하나하나 찾아서 참조관계 설정해야함.
    Set<NewAlbum> albumsAndArtists = parseForNewAlbum(artistEntities, albumEntities);

    createMusicDatabaseAllUnique(albumsAndArtists, new NewAlbumSave(albumRepository));

    Set<NewTrack> trackEntities = fetchTracksWithoutDuplicates(jsonData, new NewTrackFetch());
    Set<NewTrack> tracksAlbumsAndArtists = parseForTrack(artistEntities, albumEntities,
        trackEntities);

    createMusicDatabaseAllUnique(tracksAlbumsAndArtists, new NewTrackSave(trackRepository));
  }

  private Set<NewAlbum> parseForNewAlbum(Set<NewArtist> artists, Set<NewAlbum> albums) {
    Set<NewAlbum> newAlbums = new HashSet<>();

    for (NewAlbum album : albums) {
//      for (NewArtist artist : artists) {
//        if (album.getArtistName().equals(artist.getArtistName()))
//          album.setArtist(artist);
//      }
      List<NewArtist> artist = artistRepository.findAllByArtistSpotifyId(album.getArtistName())
          .orElseThrow(
              () -> new ContentILikeAppException(ErrorCode.NOT_FOUND, "앨범의 아티스트가 존재하지 않습니다."));

      if (!artist.isEmpty()) {
        album.setArtist(artist.get(0));
      }

      newAlbums.add(album);
    }

    return newAlbums;
  }

  private Set<NewTrack> parseForTrack(Set<NewArtist> artists, Set<NewAlbum> albums,
      Set<NewTrack> tracks) {
    Set<NewTrack> newTracks = new HashSet<>();

    for (NewTrack track : tracks) {
      List<NewAlbum> album = albumRepository.findAllByAlbumSpotifyId(track.getAlbumSpotifyId())
          .orElseThrow(
              () -> new ContentILikeAppException(ErrorCode.NOT_FOUND, "음원의 앨범이 존재하지 않습니다."));
//      NewAlbum album = albumRepository.findByAlbumTitle(track.getAlbumTitle())
//          .orElseThrow(()-> new ContentILikeAppException(ErrorCode.NOT_FOUND, "음원의 앨범이 존재하지 않습니다."));

//      NewArtist artist = artistRepository.findByArtistSpotifyId(track.getArtistSpotifyId())
//          .orElseThrow(
//              () -> new ContentILikeAppException(ErrorCode.NOT_FOUND, "음원의 아티스트가 존재하지 않습니다."));

      List<NewArtist> artist = artistRepository.findAllByArtistSpotifyId(track.getArtistSpotifyId())
          .orElseThrow(()-> new ContentILikeAppException(ErrorCode.NOT_FOUND, "음원의 아티스트가 존재하지 않습니다."));

//      genreRepofindGenreByGenreType()
      if (!album.isEmpty()) {
        track.setAlbum(album.get(0));
      }
      if (!artist.isEmpty()) {
        track.setArtist(artist.get(0));
      }

      newTracks.add(track);
    }

    return newTracks;
  }
}
