package com.content_i_like.controller;

import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.domain.dto.tracks.TrackResponse;
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
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MusicRestController.class)
class MusicRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MusicService musicService;

    @Nested
    @DisplayName("모든 음원 조회")
    class AllSongsInquiry {

        @Captor
        ArgumentCaptor<Pageable> argumentCaptor;

        @Test
        @DisplayName("성공")
        void success_get_every_song() throws Exception {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("songNo").descending());

            TrackGetResponse track = TrackGetResponse.builder()
                    .trackTitle("title")
                    .trackAlbum("album")
                    .trackArtist("artist")
                    .build();

            List<TrackGetResponse> tracks = List.of(track);

            Page<TrackGetResponse> pagedTracks = new PageImpl<>(tracks);

            given(musicService.getEveryTrack(pageable)).willReturn(pagedTracks);

            String url = "/api/v1/music/all";

            mockMvc.perform(get(url).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                    .andExpect(jsonPath("$.result.trackTitle").value("title"))
                    .andDo(print());

            verify(musicService).getEveryTrack(argumentCaptor.capture());

            Pageable actualPageable = argumentCaptor.getValue();
            assertEquals(pageable, actualPageable);
        }
    }
}