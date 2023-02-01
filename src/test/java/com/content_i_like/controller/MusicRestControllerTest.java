package com.content_i_like.controller;

import com.content_i_like.config.JwtService;
import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.domain.dto.tracks.TrackResponse;
import com.content_i_like.service.MusicService;
import com.content_i_like.service.TrackService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MusicRestController.class)
@WithMockUser
class MusicRestControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  MusicService musicService;

  @MockBean
  JwtService jwtService;

  @MockBean
  UserDetailsService userDetailsService;
  @Captor
  ArgumentCaptor<Pageable> argumentCaptor;

  @Nested
  @DisplayName("모든 음원 조회")
  class AllTracksInquiry {

    @Test
    @DisplayName("성공")
    void success_get_every_track() throws Exception {
      Pageable pageable = PageRequest.of(0, 10, Sort.by("trackNo").descending());

      TrackGetResponse track = TrackGetResponse.builder()
          .trackTitle("title")
          .trackAlbum("album")
          .trackArtist("artist")
          .build();

      List<TrackGetResponse> tracks = List.of(track);

      Page<TrackGetResponse> pagedTracks = new PageImpl<>(tracks);

      given(musicService.getEveryTrack(pageable, any())).willReturn(pagedTracks);

      String url = "/api/v1/music/all";

      mockMvc.perform(get(url).with(csrf()))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
          .andExpect(jsonPath("$.result.content").exists())
          .andDo(print());

      verify(musicService).getEveryTrack(argumentCaptor.capture(), any());

      Pageable actualPageable = argumentCaptor.getValue();
      assertEquals(pageable, actualPageable);
    }
  }
}