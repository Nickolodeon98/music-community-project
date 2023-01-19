package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.recommend.RecommendModifyRequest;
import com.content_i_like.domain.dto.recommend.RecommendModifyResponse;
import com.content_i_like.domain.dto.recommend.RecommendPostRequest;
import com.content_i_like.domain.dto.recommend.RecommendPostResponse;
import com.content_i_like.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recommends")
@RequiredArgsConstructor
public class RecommendRestController {

    private final RecommendService recommendService;

    @PostMapping
    public Response<RecommendPostResponse> uploadRecommendPost(Authentication authentication,
                                                               @RequestBody RecommendPostRequest recommendPostRequest) {
        String userEmail = authentication.getName();
        System.out.println(userEmail);
        RecommendPostResponse response = recommendService.uploadPost(userEmail, recommendPostRequest);
        return Response.success(response);
    }

    @PutMapping("/{recommendNo}")
    public Response<RecommendModifyResponse> modifyRecommendPost(Authentication authentication,
                                                                 @RequestBody RecommendModifyRequest recommendPostRequest,
                                                                 @PathVariable Long recommendNo) {
        String userEmail = authentication.getName();
        System.out.println(userEmail);
        RecommendModifyResponse response = recommendService.modifyPost(userEmail, recommendNo, recommendPostRequest);
        return Response.success(response);
    }
}
