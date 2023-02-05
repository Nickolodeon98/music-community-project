package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.notification.NotificationResponse;
import com.content_i_like.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationRestController {

    private final NotificationService notificationService;

    @GetMapping
    public Response<Slice<NotificationResponse>> getNotificationsByMember(Authentication authentication) {
        String memberName = authentication.getName();
        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by("createdAt").descending());
        Slice<NotificationResponse> memberNotifications = notificationService.getAllNotifications(memberName, pageRequest);
        return Response.success(memberNotifications);
    }

}
