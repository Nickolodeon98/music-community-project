package com.content_i_like.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.content_i_like.config.JwtService;
import com.content_i_like.controller.restcontroller.NotificationRestController;
import com.content_i_like.domain.dto.notification.NotificationResponse;
import com.content_i_like.domain.enums.NotificationType;
import com.content_i_like.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(NotificationRestController.class)
@WithMockUser
class NotificationRestControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  JwtService jwtService;

  @MockBean
  NotificationService notificationService;

  @Test
  @DisplayName("모든 알림 조회")
  void getNotificationsByMember() throws Exception {
    NotificationResponse notificationResponse1 = new NotificationResponse(1L,
        NotificationType.LIKES, 10L, 20L, null);
    NotificationResponse notificationResponse2 = new NotificationResponse(2L,
        NotificationType.COMMENTS, 11L, 21L, 30L);
    NotificationResponse notificationResponse3 = new NotificationResponse(3L,
        NotificationType.POINT_ADJUSTMENT, null, 22L, null);

    Slice<NotificationResponse> responses = new SliceImpl<>(
        List.of(notificationResponse1, notificationResponse2, notificationResponse3));

    given(notificationService.getAllNotifications(any(), any())).willReturn(responses);

    String url = "/api/v1/recommends";

    mockMvc.perform(get(url).with(csrf())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.resultCode").value("SUCCESS"))

        .andExpect(jsonPath("$['result']['content'][0]['notificationNo']").value(1L))
        .andExpect(jsonPath("$['result']['content'][0]['notificationType']").value("LIKES"))
        .andExpect(jsonPath("$['result']['content'][0]['fromMemberNo']").value(10L))
        .andExpect(jsonPath("$['result']['content'][0]['recommendNo']").value(20L))
        .andExpect(jsonPath("$['result']['content'][0]['commentNo']").doesNotExist())

        .andExpect(jsonPath("$['result']['content'][1]['notificationNo']").value(2L))
        .andExpect(jsonPath("$['result']['content'][1]['notificationType']").value("COMMENTS"))
        .andExpect(jsonPath("$['result']['content'][1]['fromMemberNo']").value(11L))
        .andExpect(jsonPath("$['result']['content'][1]['recommendNo']").value(21L))
        .andExpect(jsonPath("$['result']['content'][1]['commentNo']").value(30L))

        .andExpect(jsonPath("$['result']['content'][2]['notificationNo']").value(3L))
        .andExpect(jsonPath("$['result']['content'][2]['notificationType']").value("POINT_ADJUSTMENT"))
        .andExpect(jsonPath("$['result']['content'][2]['fromMemberNo']").doesNotExist())
        .andExpect(jsonPath("$['result']['content'][2]['recommendNo']").value(22L))
        .andExpect(jsonPath("$['result']['content'][2]['commentNo']").doesNotExist())
        .andDo(print());

  }
}