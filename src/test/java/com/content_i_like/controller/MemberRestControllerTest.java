package com.content_i_like.controller;

import com.content_i_like.config.JwtService;
import com.content_i_like.domain.dto.member.MemberJoinRequest;
import com.content_i_like.domain.dto.member.MemberJoinResponse;
import com.content_i_like.domain.dto.member.MemberLoginRequest;
import com.content_i_like.domain.dto.member.MemberLoginResponse;
import com.content_i_like.domain.enums.GenderEnum;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.service.MailService;
import com.content_i_like.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberRestController.class)
@WebAppConfiguration
@WithMockUser
class MemberRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    JwtService jwtService;

    @MockBean
    MemberService memberService;

    @MockBean
    MailService mailService;

    @Test
    @DisplayName("회원가입 성공")
    @WithMockUser
    void join_success() throws Exception {
        MemberJoinRequest memberJoinRequest = MemberJoinRequest.builder()
                .name("rnjsthdus")
                .nickName("test")
                .email("test@gmail.com")
                .password("123456789")
                .gender(GenderEnum.UNKNOWN)
                .birth(0)
                .build();
        MemberJoinResponse memberJoinResponse = new MemberJoinResponse(1L, "nickname");

        Mockito.when(memberService.join(any())).thenReturn(memberJoinResponse);

        mockMvc.perform(post("/api/v1/member/join")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(memberJoinRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.no").value(1))
                .andExpect(jsonPath("$.result.nickName").value("nickname"));
    }

    @Test
    @DisplayName("회원가입 실패")
    void join_fail() throws Exception {
        MemberJoinRequest memberJoinRequest = MemberJoinRequest.builder()
                .name("rnjsthdus")
                .nickName("test")
                .email("test@gmail.com")
                .password("123456789")
                .gender(GenderEnum.UNKNOWN)
                .birth(0)
                .build();

        Mockito.when(memberService.join(any())).thenThrow(new ContentILikeAppException(ErrorCode.NOT_FOUND,""));

        mockMvc.perform(post("/api/v1/member/join")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(memberJoinRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그인 성공")
    @WithMockUser
    void login_success() throws Exception {
        MemberLoginRequest request = new MemberLoginRequest("test@gmail.com","123456789");
        MemberLoginResponse response = new MemberLoginResponse("token","nick");


        Mockito.when(memberService.login(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/member/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.jwt").value("token"))
                .andExpect(jsonPath("$.result.nickName").value("nick"));
    }
}