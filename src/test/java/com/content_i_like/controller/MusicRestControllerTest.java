package com.content_i_like.controller;

import com.content_i_like.config.JwtService;
import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.domain.dto.tracks.TrackResponse;
import com.content_i_like.service.MusicService;
import com.content_i_like.service.TrackService;
import org.junit.jupiter.api.BeforeEach;
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
    Pageable pageable;
    TrackGetResponse foundTrack;
    Page<TrackGetResponse> pagedTracks;
    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10, Sort.by("songNo").descending());
        foundTrack = TrackGetResponse.builder()
                .trackTitle("Event Horizon")
                .trackArtist("Younha")
                .trackAlbum("YOUNHA 6th Album Repackage 'END THEORY : Final Edition'")
                .build();
        pagedTracks = new PageImpl<>(List.of(foundTrack));
    }

    @Nested
    @DisplayName("모든 음원 조회")
    class AllSongsInquiry {

        @Test
        @DisplayName("성공")
        void success_get_every_song() throws Exception {
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

    @Nested
    @DisplayName("검색어에 해당되는 음원 조회")
    class SearchTrackByTitle {
        @Test
        @DisplayName("성공")
        void success_search_by_keyword() throws Exception {
            String searchKey = "Horizon";

            given(musicService.findSongsWithKeyword(pageable, searchKey)).willReturn(pagedTracks);

            String url = "api/v1/music/search/" + searchKey;

            mockMvc.perform(get(url).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                    .andExpect(jsonPath("$.result.content.trackTitle").value("Event Horizon"))
                    .andExpect(jsonPath("$.resultCode.content.trackArtist").value("Younha"))
                    .andExpect(jsonPath("$.resultCode.content.trackAlbum")
                            .value("YOUNHA 6th Album Repackage 'END THEORY : Final Edition'"))
                    .andDo(print());
        }
    }
}