package com.content_i_like.controller;

import com.content_i_like.config.JwtService;
import com.content_i_like.controller.restcontroller.MemberRestController;
import com.content_i_like.domain.dto.member.*;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.enums.GenderEnum;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.service.MailService;
import com.content_i_like.service.MemberService;
import com.content_i_like.service.CustomOAuth2UserService;
import com.content_i_like.service.PointService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberRestController.class)
@WebAppConfiguration
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
  PointService pointService;

  @MockBean
  MailService mailService;

  @MockBean
  CustomOAuth2UserService oAuthService;

  @MockBean
  UserDetailsService userDetailsService;

  MemberJoinRequest memberJoinRequest;
  MemberJoinResponse memberJoinResponse;
  Member member;

  @BeforeEach
  public void set() {
    memberJoinRequest = MemberJoinRequest.builder()
        .name("rnjsthdus")
        .nickName("test")
        .email("test@gmail.com")
        .password("rnBsthdus57@")
        .gender(GenderEnum.UNKNOWN)
        .birth(0)
        .build();
    memberJoinResponse = new MemberJoinResponse(1l, "nickname");
  }

  @Test
  @DisplayName("회원가입 성공")
  @WithMockUser
  void join_success() throws Exception {
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
  @DisplayName("회원가입 실패 - 중복된 이메일 혹은 닉네임")
  @WithMockUser
  void join_fail() throws Exception {

    Mockito.when(memberService.join(any()))
        .thenThrow(new ContentILikeAppException(ErrorCode.DUPLICATED_MEMBER_NAME, ""));

    mockMvc.perform(post("/api/v1/member/join")
        .with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(memberJoinRequest)))
        .andDo(print())
        .andExpect(status().isConflict());
  }

  @Test
  @DisplayName("로그인 성공")
  @WithMockUser
  void login_success() throws Exception {
    MemberLoginRequest request = new MemberLoginRequest("test@gmail.com", "123456789");
    MemberLoginResponse response = new MemberLoginResponse("token",1l,"nick");

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

  @Test
  @DisplayName("로그인 실패1 - email 없음")
  @WithMockUser
  void login_fail1() throws Exception {
    MemberLoginRequest request = new MemberLoginRequest("test@gmail.com", "123456789");

    Mockito.when(memberService.login(any()))
        .thenThrow(new ContentILikeAppException(ErrorCode.MEMBER_NOT_FOUND, ""));

    mockMvc.perform(post("/api/v1/member/login")
        .with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(request)))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.resultCode").value("ERROR"));
  }

  @Test
  @DisplayName("로그인 실패2 - password 틀림")
  @WithMockUser
  void login_fail2() throws Exception {
    MemberLoginRequest request = new MemberLoginRequest("test@gmail.com", "123456789");

    Mockito.when(memberService.login(any()))
        .thenThrow(new ContentILikeAppException(ErrorCode.INVALID_PASSWORD, ""));

    mockMvc.perform(post("/api/v1/member/login")
        .with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(request)))
        .andDo(print())
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.resultCode").value("ERROR"));
  }

  @Test
  @DisplayName("비밀번호 변경 성공")
  @WithMockUser
  void changePw_success() throws Exception {
    ChangePwRequest request = new ChangePwRequest("newpassword12?", "newpassword12?");

    Mockito.when(memberService.changePw(any(), any()))
        .thenReturn(request.getNewPassword());

    mockMvc.perform(post("/api/v1/member/passwd/change")
        .with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result").value(request.getNewPassword()));
  }

  @Test
  @DisplayName("내 정보 보기 성공")
  @WithMockUser
  void getMyInfo_success() throws Exception {
    MemberResponse response = MemberResponse.builder()
        .email("test@gmail.com")
        .nickName("nickname")
        .point(1000l)
        .build();

    Mockito.when(memberService.getMyInfo(any())).thenReturn(response);
    Mockito.when(pointService.calculatePoint(any())).thenReturn(1000l);

    mockMvc.perform(get("/api/v1/member/my")
        .with(csrf())
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
        .andExpect(jsonPath("$.result.email").value(response.getEmail()))
        .andExpect(jsonPath("$.result.point").value(1000l));
  }

  @Test
  @DisplayName("내 정보 수정 성공")
  @WithMockUser
  void modifyMyInfo_success() throws Exception {
    MockMultipartFile mockPart = new MockMultipartFile("file", "filename.png", "image/png",
        "file".getBytes());
    MemberModifyRequest request = MemberModifyRequest.builder()
        .introduction("")
        .gender(GenderEnum.FEMALE)
        .build();
    MemberResponse response = MemberResponse.builder()
        .email("test@gmail.com")
        .nickName("nickname")
        .introduction("")
        .gender("FEMALE")
        .build();

    Mockito.when(memberService.modifyMyInfo(any(), eq(mockPart), any())).thenReturn(response);

//        mockMvc.perform(multipart("/api/v1/member/my")
//                .file(mockPart)
//                .file(new MockMultipartFile("dto","","application/json",objectMapper.writeValueAsBytes(request)))
//                .with(csrf()))
//
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.resultCode").value("SUCCESS"));
  }

}