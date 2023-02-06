package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.notification.NotificationResponse;
import com.content_i_like.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public Response<Slice<NotificationResponse>> getNotificationsByMember(Authentication authentication) {
        String memberName = authentication.getName();
        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by("createdAt").descending());
        Slice<NotificationResponse> memberNotifications = notificationService.getAllNotifications(memberName, pageRequest);
        return Response.success(memberNotifications);
    }

}
