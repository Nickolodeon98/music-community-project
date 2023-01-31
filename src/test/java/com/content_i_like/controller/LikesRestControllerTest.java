package com.content_i_like.controller;

import com.content_i_like.config.JwtService;
import com.content_i_like.domain.entity.*;
import com.content_i_like.fixture.Fixture;
import com.content_i_like.service.LikesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LikesRestController.class)
@WithMockUser
class LikesRestControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  LikesService likesService;

  @MockBean
  UserDetailsService userDetailsService;
  @MockBean
  JwtService jwtService;


  Artist artist;
  Song song;
  Album album;
  Recommend recommend;
  Member member;
  Comment comment;

  @BeforeEach
  public void set() {
    artist = Fixture.getArtistFixture();
    album = Fixture.getAlbumFixture(artist);
    song = Fixture.getSongFixture(album);

    member = Fixture.getMemberFixture();
    recommend = Fixture.getRecommendFixture(member, song);
    comment = Fixture.getCommentFixture(member, recommend);
  }

  String jwtToken;

  @BeforeEach
  void getToken() {
    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    jwtToken = Jwts.builder()
        .setSubject("user")
        .setIssuer("issuer")
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  @Test
  @DisplayName("좋아요 활성화")
  void success_active_comment() throws Exception {

    given(likesService.changeLikesStatus(any(), any())).willReturn("좋아요를 눌렀습니다");

    String url = "/api/v1/recommends/1/likes";

    mockMvc.perform(post(url).with(csrf())
            .header(HttpHeaders.AUTHORIZATION, jwtToken)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.resultCode").exists())
        .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
        .andExpect(jsonPath("$.result").exists())
        .andExpect(jsonPath("$.result").value("좋아요를 눌렀습니다"))
        .andDo(print());
  }

  @Test
  @DisplayName("좋아요 비활성화")
  void success_inactive_comment() throws Exception {

    given(likesService.changeLikesStatus(any(), any())).willReturn("좋아요를 취소했습니다.");

    String url = "/api/v1/recommends/1/likes";

    mockMvc.perform(post(url).with(csrf())
            .header(HttpHeaders.AUTHORIZATION, jwtToken)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.resultCode").exists())
        .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
        .andExpect(jsonPath("$.result").exists())
        .andExpect(jsonPath("$.result").value("좋아요를 취소했습니다."))
        .andDo(print());
  }

  @Test
  @DisplayName("좋아요 개수 반환")
  void success_return_number_likes() throws Exception {

    given(likesService.countNumberLikes(any())).willReturn(123);

    String url = "/api/v1/recommends/1/likes";

    mockMvc.perform(get(url).with(csrf())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.resultCode").exists())
        .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
        .andExpect(jsonPath("$.result").exists())
        .andExpect(jsonPath("$.result").value(123))
        .andDo(print());
  }
}