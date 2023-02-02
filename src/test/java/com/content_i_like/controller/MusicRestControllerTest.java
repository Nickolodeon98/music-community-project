package com.content_i_like.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.fixture.Fixture;
import com.content_i_like.service.MusicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser
@WebMvcTest(MusicRestController.class)
class MusicRestControllerTest {

  @MockBean
  MusicService musicService;

  @Autowired
  MockMvc mockMvc;

  final String BASE_URL = "/api/v1/music/";
  TrackGetResponse track;

  final Long TRACK_ID = 1L;

  @BeforeEach
  void setUp() {
    track = TrackGetResponse.of(Fixture.getTrackFixture(Fixture.getAlbumFixture(Fixture.getArtistFixture())));
  }

  @Nested
  @DisplayName("음원 한개 상세 조회")
  class DetailedTrackInfo {
    @Test
    @DisplayName("성공")
    void success_specify_a_track() throws Exception {
      given(musicService.getASingleTrackInfo(TRACK_ID, any())).willReturn(track);

      mockMvc.perform(get(BASE_URL + "track/" + TRACK_ID).with(csrf()))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
          .andDo(print());

      verify(musicService).getASingleTrackInfo(TRACK_ID, any());
    }
  }
}