package com.content_i_like.controller;

import com.content_i_like.config.JwtService;
import com.content_i_like.custom.WithMockCustomOAuth2Account;
import com.content_i_like.domain.dto.recommend.*;
import com.content_i_like.domain.entity.*;
import com.content_i_like.fixture.Fixture;
import com.content_i_like.service.RecommendService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecommendRestController.class)
@WithMockCustomOAuth2Account(registrationId = "google")
@WithUserDetails
class RecommendRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    RecommendService recommendService;

    @MockBean
    UserDetailsService userDetailsService;

    @MockBean
    JwtService jwtService;


    Artist artist;
    Song song;
    Album album;
    Recommend recommend;
    Member member;

    @BeforeEach
    public void set() {
        artist = Fixture.getArtistFixture();

        album = Fixture.getAlbumFixture(artist);

        song = Fixture.getSongFixture(album);

        member = Fixture.getMemberFixture();

        recommend = Fixture.getRecommendFixture(member, song);

    }

    private String access_token;

//  @BeforeEach
//  public void getAccessToken() throws Exception {
//    String clientId = "";
//    String clientSecret = "";
//    String username = "";
//    String password = "";
//
//    ResultActions perform = this.mockMvc.perform(post("/oauth2/authorization/google")
//            .with(httpBasic(clientId, clientSecret))
//            .param("username", username)
//            .param("password", password)
//            .param("grant_type", "password"));
//
//    this.access_token = (String) new Jackson2JsonParser().parseMap(perform.andReturn().getResponse().getContentAsString()).get("access_token");
//  }


    @Test
    @DisplayName("추천글 작성")
    void success_post_recommend() throws Exception {

        RecommendPostRequest request = new RecommendPostRequest("제목", "내용", "유튜브", 100L, 1L);
        RecommendPostResponse response = new RecommendPostResponse(1L, "제목", 100L);

        given(recommendService.uploadPost(any(), any(), any())).willReturn(response);

        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());

        String url = "/api/v1/recommends";

        MockMultipartFile jsonPart = new MockMultipartFile("request", "request.json", "application/json", objectMapper.writeValueAsBytes(request));

        mockMvc.perform(multipart(url)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("request", new ObjectMapper().writeValueAsString(request))
                        .with(csrf()))
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
        /*mockMvc.perform(multipart(url)
                        .file(file)
                        .file(jsonPart)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.recommendNo").exists())
                .andExpect(jsonPath("$.result.recommendNo").value(1))
                .andExpect(jsonPath("$.result.recommendTitle").exists())
                .andExpect(jsonPath("$.result.recommendTitle").value("제목"))
                .andExpect(jsonPath("$.result.recommendPoint").exists())
                .andExpect(jsonPath("$.result.recommendPoint").value(100L))
                .andDo(print());*/
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
    @DisplayName("추천글 읽기")
    void success_read_recommend() throws Exception {
        ArrayList<Long> commentPoints = new ArrayList<>();
        ArrayList<Comment> comments = new ArrayList<>();

        RecommendReadResponse response = RecommendReadResponse
                .builder()
                .recommendTitle("제목")
                .memberNickname("작성자")
                .albumImageUrl("앨범 이미지")
                .songTitle("노래 제목")
                .artistName("아티스트 이름")
                .recommendContent("추천 내용")
                .countLikes(100L)
                .recommendPoint(1000L)
                .accumulatedPoints(1200L)
                .recommendYoutubeUrl("유튜브 링크")
                .comments(comments)
                .build();

        given(recommendService.readPost(any())).willReturn(response);

        String url = String.format("/api/v1/recommends/%d", 1);

        mockMvc.perform(get(url).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.recommendTitle").exists())
                .andExpect(jsonPath("$.result.recommendTitle").value("제목"))
                .andExpect(jsonPath("$.result.memberNickname").exists())
                .andExpect(jsonPath("$.result.memberNickname").value("작성자"))
                .andExpect(jsonPath("$.result.albumImageUrl").exists())
                .andExpect(jsonPath("$.result.albumImageUrl").value("앨범 이미지"))
                .andExpect(jsonPath("$.result.songTitle").exists())
                .andExpect(jsonPath("$.result.songTitle").value("노래 제목"))
                .andExpect(jsonPath("$.result.comments").exists())
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