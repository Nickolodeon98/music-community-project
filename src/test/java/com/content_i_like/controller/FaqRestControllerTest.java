package com.content_i_like.controller;

import com.content_i_like.config.JwtService;
import com.content_i_like.service.FaqService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FaqRestControllerTest.class)
@WebAppConfiguration
class FaqRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FaqService faqService;

    @MockBean
    JwtService jwtService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("faq 전체조회 잘 작동하는지")
    void faq() throws Exception {

//        when(faqService.getAllFaq(any()))
//                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/faq")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }
}