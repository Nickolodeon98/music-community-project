package com.content_i_like.controller;

import com.content_i_like.config.JwtService;
import com.content_i_like.domain.dto.answer.AnswerRequire;
import com.content_i_like.domain.dto.answer.AnswerResponse;
import com.content_i_like.domain.dto.inquiry.InquiryRequire;
import com.content_i_like.domain.dto.inquiry.InquiryResponse;
import com.content_i_like.service.AnswerService;
import com.content_i_like.service.FaqService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnswerRestController.class)
@WebAppConfiguration
@WithMockUser
class AnswerRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AnswerService answerService;

    @MockBean
    JwtService jwtService;

    @MockBean
    UserDetailsService userDetailsService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("문의내역 답변 불러오기 잘 되는지")
    void answer_get_success() throws Exception {
        when(answerService.getAnswer(any()))
                .thenReturn(AnswerResponse.builder()
                        .answerNo(1L)
                        .createdAt(now())
                        .content("testAnswer")
                        .build());

        mockMvc.perform(get("/api/v1/answer/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.answerNo").exists())
                .andExpect(jsonPath("$.result.createdAt").exists())
                .andExpect(jsonPath("$.result.content").exists())
        ;
    }

    @Test
    @DisplayName("문의내역 답변 등록하기 잘 되는지")
    void answer_post_success() throws Exception {
        AnswerRequire answerRequire = AnswerRequire.builder()
                .content("testAnswer")
                .build();

        when(answerService.postAnswer(any(), any()))
                .thenReturn(AnswerResponse.builder()
                        .answerNo(1L)
                        .createdAt(now())
                        .content("testAnswer")
                        .build());

        mockMvc.perform(post("/api/v1/answer/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(answerRequire)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.answerNo").exists())
                .andExpect(jsonPath("$.result.createdAt").exists())
                .andExpect(jsonPath("$.result.content").exists())
        ;
    }
}