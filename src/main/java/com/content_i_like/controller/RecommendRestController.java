package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.recommend.RecommendPostRequest;
import com.content_i_like.domain.dto.recommend.RecommendResponse;
import com.content_i_like.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recommends")
@RequiredArgsConstructor
public class RecommendRestController {

    private final RecommendService recommendService;

    @PostMapping
    public Response<RecommendResponse> uploadRecommendPost(Authentication authentication,
                                                           @RequestBody RecommendPostRequest recommendPostRequest) {
        String userEmail = authentication.getName();
        System.out.println(userEmail);
        RecommendResponse response = recommendService.uploadPost(userEmail, recommendPostRequest);
        return Response.success(response);
    }
}
