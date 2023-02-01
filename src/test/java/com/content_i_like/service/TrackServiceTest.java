package com.content_i_like.service;

import com.content_i_like.repository.AlbumRepository;
import com.content_i_like.repository.ArtistRepository;
import com.content_i_like.repository.TrackRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;


class TrackServiceTest {


  @Mock
  RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

  TrackRepository trackRepository = Mockito.mock(TrackRepository.class);

  ArtistRepository artistRepository = Mockito.mock(ArtistRepository.class);
  AlbumRepository albumRepository = Mockito.mock(AlbumRepository.class);
  TrackService trackService = new TrackService(new ObjectMapper(), trackRepository, artistRepository, albumRepository);

  final String CODE =
          "AQBO1RY1Fd0nhdxGNZmg6PmmFuZ0tJauQq7AliyEOKC8jR04QRStsscveeovc_FyydCYxx0w5avPCQqCUEmBLl25-3lI"
                  +
                  "FmEYU5t9KAL_LkxbI0_2P4Lii7zz4bHPi9b5LO2_x62Ki_j-t3OSf2oK_l3OmA7MYeRUUnKuPhgn-WqoEKvsBg";

  @Test
  @DisplayName("스포티파이 토큰을 발급한다")
  void success_generate_spotify_token() throws JsonProcessingException {
    System.out.println(trackService.spotifyAccessTokenGenerator(CODE));
  }
}