package com.content_i_like.controller;

import com.content_i_like.config.JwtService;
import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.domain.dto.tracks.TrackPageGetResponse;
import com.content_i_like.service.SearchService;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SearchRestController.class)
@WithMockUser
class SearchRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SearchService searchService;

    @MockBean
    JwtService jwtService;

    @MockBean
    UserDetailsService userDetailsService;
    @Captor
    ArgumentCaptor<Pageable> argumentCaptor;
    Pageable pageable;
    TrackGetResponse foundTrack;
    Page<TrackGetResponse> pagedTracks;
    TrackPageGetResponse pagedTracksWithMessage;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10, Sort.by("trackNo").descending());
        foundTrack = TrackGetResponse.builder()
                .trackTitle("Event Horizon")
                .trackArtist("Younha")
                .trackAlbum("YOUNHA 6th Album Repackage 'END THEORY : Final Edition'")
                .build();
        pagedTracks = new PageImpl<>(List.of(foundTrack));
        pagedTracksWithMessage = TrackPageGetResponse.builder()
                .message("총 " + pagedTracks.getTotalElements() + "개의 음원을 찾았습니다.")
                .tracks(pagedTracks)
                .build();
    }

    @Nested
    @DisplayName("모든 음원 조회")
    class AllTracksInquiry {

        @Test
        @DisplayName("성공")
        void success_get_every_track() throws Exception {
            given(searchService.getEveryTrack(eq(pageable), any())).willReturn(pagedTracksWithMessage);

            String url = "/api/v1/music/search";

            mockMvc.perform(get(url).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                    .andExpect(jsonPath("$.result.tracks.content").exists())
                    .andDo(print());

            verify(searchService).getEveryTrack(argumentCaptor.capture(), any());

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
            given(searchService.findTracksWithKeyword(eq(pageable), eq(searchKey), any())).willReturn(pagedTracksWithMessage);

            String url = "/api/v1/music/search/" + searchKey;

            mockMvc.perform(get(url).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                    .andExpect(jsonPath("$.result.tracks.content[0].trackTitle").value("Event Horizon"))
                    .andExpect(jsonPath("$.result.tracks.content[0].trackArtist").value("Younha"))
                    .andExpect(jsonPath("$.result.tracks.content[0].trackAlbum")
                            .value("YOUNHA 6th Album Repackage 'END THEORY : Final Edition'"))
                    .andDo(print());

            verify(searchService).findTracksWithKeyword(eq(pageable), eq(searchKey), any());
        }
    }
}