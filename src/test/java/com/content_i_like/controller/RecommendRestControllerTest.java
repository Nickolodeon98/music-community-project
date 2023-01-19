package com.content_i_like.controller;

import com.content_i_like.config.JwtService;
import com.content_i_like.domain.dto.recommend.RecommendModifyRequest;
import com.content_i_like.domain.dto.recommend.RecommendModifyResponse;
import com.content_i_like.domain.dto.recommend.RecommendPostRequest;
import com.content_i_like.domain.dto.recommend.RecommendPostResponse;
import com.content_i_like.domain.entity.Album;
import com.content_i_like.domain.entity.Artist;
import com.content_i_like.domain.entity.Recommend;
import com.content_i_like.domain.entity.Song;
import com.content_i_like.service.RecommendService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecommendRestController.class)
@WebAppConfiguration
@WithMockUser
class RecommendRestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    RecommendService recommendService;

    @MockBean
    JwtService jwtService;


    Artist artist;
    Song song;
    Album album;
    Recommend recommend;

    @BeforeEach
    public void set() {
        artist = Artist.builder()
                .artistNo(1L)
                .artistName("이름")
                .build();

        album = Album.builder()
                .albumNo(1L)
                .albumImageUrl("이미지")
                .artist(artist)
                .build();

        song = Song.builder()
                .songNo(1L)
                .songTitle("음원 제목")
                .album(album)
                .build();

        recommend = Recommend.builder()
                .recommendNo(1L)
                .recommendTitle("제목")
                .recommendContent("내용")
                .recommendImageUrl("이미지")
                .recommendYoutubeUrl("유튜브")
                .recommendPoint(1000L)
                .recommendViews(100L)
                .song(song)
                .build();
    }

    @Test
    @DisplayName("추천글 작성")
    void success_post_recommend() throws Exception {

        RecommendPostRequest request = new RecommendPostRequest("제목", "내용", "이미지", "유튜브", 100L, 1L);
        RecommendPostResponse response = new RecommendPostResponse(1L, "제목", 100L);

        given(recommendService.uploadPost(any(), any())).willReturn(response);

        String url = "/api/v1/recommends";

        mockMvc.perform(post(url).with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.recommendNo").exists())
                .andExpect(jsonPath("$.result.recommendNo").value(1))
                .andExpect(jsonPath("$.result.recommendTitle").exists())
                .andExpect(jsonPath("$.result.recommendTitle").value("제목"))
                .andExpect(jsonPath("$.result.recommendPoint").exists())
                .andExpect(jsonPath("$.result.recommendPoint").value(100L))
                .andDo(print());
    }

    @Test
    @DisplayName("추천글 수정")
    void success_modify_recommend() throws Exception {

        RecommendModifyRequest request = new RecommendModifyRequest("수정", "수정내용", "수정 이미지", "수정 유튜브");
        RecommendModifyResponse response = new RecommendModifyResponse(1L, "수정");

        given(recommendService.modifyPost(any(), any(), any())).willReturn(response);

        String url = String.format("/api/v1/recommends/%d", response.getRecommendNo());

        mockMvc.perform(put(url).with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.recommendNo").exists())
                .andExpect(jsonPath("$.result.recommendNo").value(1))
                .andExpect(jsonPath("$.result.recommendTitle").exists())
                .andExpect(jsonPath("$.result.recommendTitle").value("수정"))
                .andDo(print());
    }

    @Test
    @DisplayName("추천글 삭제")
    void success_delete_recommend() throws Exception {
        doNothing().when(recommendService).deletePost(any(), any());

        String url = String.format("/api/v1/recommends/%d", 1);

        mockMvc.perform(delete(url).with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.recommendNo").exists())
                .andExpect(jsonPath("$.result.recommendNo").value(1))
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.message").value("추천 글이 삭제 되었습니다."))
                .andDo(print());
    }
}