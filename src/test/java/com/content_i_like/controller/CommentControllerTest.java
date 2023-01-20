package com.content_i_like.controller;

import com.content_i_like.config.JwtService;
import com.content_i_like.domain.dto.comment.CommentModifyRequest;
import com.content_i_like.domain.dto.comment.CommentRequest;
import com.content_i_like.domain.dto.comment.CommentResponse;
import com.content_i_like.domain.entity.*;
import com.content_i_like.fixture.Fixture;
import com.content_i_like.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@WithMockUser
class CommentControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CommentService commentService;

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

    @Test
    @DisplayName("댓글 작성")
    void success_write_comment() throws Exception {
        CommentRequest request = new CommentRequest("댓글", 100L);
        CommentResponse response = new CommentResponse(1L, 1L, "댓글", 100L);

        given(commentService.writeComment(any(), any(), any())).willReturn(response);

        String url = "/api/v1/recommends/1/comments";

        mockMvc.perform(post(url).with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.commentNo").exists())
                .andExpect(jsonPath("$.result.commentNo").value(1))
                .andExpect(jsonPath("$.result.postNo").exists())
                .andExpect(jsonPath("$.result.postNo").value(1))
                .andExpect(jsonPath("$.result.commentContent").exists())
                .andExpect(jsonPath("$.result.commentContent").value("댓글"))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 수정")
    void success_modify_comment() throws Exception {
        CommentModifyRequest request = new CommentModifyRequest("댓글 수정");
        CommentResponse response = new CommentResponse(1L, 1L, "댓글 수정", 100L);

        given(commentService.modifyComment(any(), any(), any(), any())).willReturn(response);

        String url = String.format("/api/v1/recommends/1/comments/%d", 1);

        mockMvc.perform(put(url).with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.commentNo").exists())
                .andExpect(jsonPath("$.result.commentNo").value(1))
                .andExpect(jsonPath("$.result.postNo").exists())
                .andExpect(jsonPath("$.result.postNo").value(1))
                .andExpect(jsonPath("$.result.commentContent").exists())
                .andExpect(jsonPath("$.result.commentContent").value("댓글 수정"))
                .andDo(print());
    }
}