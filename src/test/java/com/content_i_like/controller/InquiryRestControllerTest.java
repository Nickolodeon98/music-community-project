package com.content_i_like.controller;

import com.content_i_like.config.JwtService;
import com.content_i_like.domain.dto.inquiry.InquiryRequire;
import com.content_i_like.domain.dto.inquiry.InquiryResponse;
import com.content_i_like.service.InquiryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InquiryRestController.class)
@WebAppConfiguration
@WithMockUser
class InquiryRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    InquiryService inquiryService;

    @MockBean
    JwtService jwtService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("1:1 문의 등록 잘되는지")
    void post_inquiry_success() throws Exception {
        InquiryRequire inquiryRequire = InquiryRequire.builder()
                .title("testTitle")
                .content("testContent")
                .build();

        when(inquiryService.postInquiry(any(), any()))
                .thenReturn(InquiryResponse.builder()
                        .inquiryNo(1L)
                        .title("testTitle")
                        .createdAt(now())
                        .processingStatus("처리중")
                        .build());

        mockMvc.perform(post("/api/v1/inquiry")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(inquiryRequire)))
                    .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.inquiryNo").exists())
                .andExpect(jsonPath("$.result.title").exists())
                .andExpect(jsonPath("$.result.createdAt").exists())
                .andExpect(jsonPath("$.result.processingStatus").exists())
                ;
    }

    @Test
    @DisplayName("1:1 문의내역 조회 잘 되는지")
    void name() throws Exception {
        when(inquiryService.getInquiryList(any(), any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/inquiry")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
        ;

    }
}