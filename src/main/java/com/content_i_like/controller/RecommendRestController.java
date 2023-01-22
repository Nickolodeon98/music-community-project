package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.RecommendDeleteResponse;
import com.content_i_like.domain.dto.recommend.*;
import com.content_i_like.service.RecommendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recommends")
@RequiredArgsConstructor
@Slf4j
public class RecommendRestController {

    private final RecommendService recommendService;

    @PostMapping
    public Response<RecommendPostResponse> uploadRecommendPost(final Authentication authentication,
                                                               @RequestBody @Valid final RecommendPostRequest request) {
        String userEmail = authentication.getName();
        log.info("user_email = {}, recommend_post_request = {}", userEmail, request);

        RecommendPostResponse response = recommendService.uploadPost(userEmail, request);
        return Response.success(response);
    }

    @PutMapping("/{recommendNo}")
    public Response<RecommendModifyResponse> modifyRecommendPost(final Authentication authentication,
                                                                 @RequestBody @Valid final RecommendModifyRequest request,
                                                                 @PathVariable final Long recommendNo) {
        String userEmail = authentication.getName();
        log.info("user_email = {}, recommend_modify_request = {}", userEmail, request);

        RecommendModifyResponse response = recommendService.modifyPost(userEmail, recommendNo, request);
        return Response.success(response);
    }

    @DeleteMapping("/{recommendNo}")
    public Response<RecommendDeleteResponse> deleteRecommendPost(final Authentication authentication,
                                                                 @PathVariable final Long recommendNo) {
        String userEmail = authentication.getName();
        log.info("user email = {}, recommend_no = {}", userEmail, recommendNo);

        recommendService.deletePost(userEmail, recommendNo);
        return Response.success(new RecommendDeleteResponse(recommendNo, "추천 글이 삭제 되었습니다."));
    }

    @GetMapping("/{recommendNo}")
    public Response<RecommendReadResponse> ReadRecommendPost(@PathVariable final Long recommendNo) {
        log.info("recommend_no = {}", recommendNo);

        RecommendReadResponse response = recommendService.readPost(recommendNo);
        return Response.success(response);
    }
}
