package com.content_i_like.controller;

import com.content_i_like.config.JwtService;
import com.content_i_like.domain.dto.comment.CommentModifyRequest;
import com.content_i_like.domain.dto.comment.CommentReadResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentRestController.class)
@WithMockUser
class CommentControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CommentService commentService;

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

    @Test
    @DisplayName("댓글 삭제")
    void success_delete_comment() throws Exception {
        doNothing().when(commentService).deleteComment(any(), any(), any());

        String url = String.format("/api/v1/recommends/1/comments/%d", 1);

        mockMvc.perform(delete(url).with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.commentNo").exists())
                .andExpect(jsonPath("$.result.commentNo").value(1))
                .andExpect(jsonPath("$.result.recommendNo").exists())
                .andExpect(jsonPath("$.result.recommendNo").value(1))
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.message").value("댓글이 삭제 되었습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 조회")
    void success_get_comment() throws Exception {
        CommentReadResponse response = new CommentReadResponse(1L, "chordpli", "image", "body", 100L, LocalDateTime.now());

        given(commentService.getReadComment(any(), any())).willReturn(response);

        String url = String.format("/api/v1/recommends/1/comments/%d", 1);

        mockMvc.perform(get(url).with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.memberNickname").exists())
                .andExpect(jsonPath("$.result.memberNickname").value("chordpli"))
                .andExpect(jsonPath("$.result.profileImgUrl").exists())
                .andExpect(jsonPath("$.result.profileImgUrl").value("image"))
                .andExpect(jsonPath("$.result.commentContent").exists())
                .andExpect(jsonPath("$.result.commentContent").value("body"))
                .andExpect(jsonPath("$.result.commentPoint").exists())
                .andExpect(jsonPath("$.result.commentPoint").value(100L))
                .andExpect(jsonPath("$.result.createdAt").exists())
                .andDo(print());
    }


    @Test
    @DisplayName("게시글 모든 댓글 조회")
    void success_get_all_comment() throws Exception {
        CommentReadResponse comment_01 = new CommentReadResponse(1L,"chordpli", "image", "body", 100L, LocalDateTime.now());
        CommentReadResponse comment_02 = new CommentReadResponse(2l, "chordpli2", "image2", "body2", 200L, LocalDateTime.now());
        CommentReadResponse comment_03 = new CommentReadResponse(3l, "chordpli3", "image3", "body3", 300L, LocalDateTime.now());
        Page<CommentReadResponse> response = new PageImpl<>(List.of(comment_01, comment_02, comment_03));

        given(commentService.getReadAllComment(any(), any())).willReturn(response);

        String url = "/api/v1/recommends/1/comments";

        mockMvc.perform(get(url).with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$['result']['content'][0]['memberNickname']").exists())
                .andExpect(jsonPath("$['result']['content'][0]['memberNickname']").value("chordpli"))
                .andExpect(jsonPath("$['result']['content'][0]['profileImgUrl']").exists())
                .andExpect(jsonPath("$['result']['content'][0]['profileImgUrl']").value("image"))
                .andExpect(jsonPath("$['result']['content'][0]['commentContent']").exists())
                .andExpect(jsonPath("$['result']['content'][0]['commentContent']").value("body"))
                .andExpect(jsonPath("$['result']['content'][0]['commentPoint']").exists())
                .andExpect(jsonPath("$['result']['content'][0]['commentPoint']").value(100L))
                .andExpect(jsonPath("$['result']['content'][0]['createdAt']").exists())
                .andDo(print());
    }
}