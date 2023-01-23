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

    /**
     * 추천 글을 작성합니다.
     *
     * @param authentication header의 token
     * @param request 등록할 추천글 정보
     * @return 등록된 추천글 정보
     */
    @PostMapping
    public Response<RecommendPostResponse> uploadRecommendPost(final Authentication authentication,
                                                               @RequestBody @Valid final RecommendPostRequest request) {
        String userEmail = authentication.getName();
        log.info("user_email = {}, recommend_post_request = {}", userEmail, request);

        RecommendPostResponse response = recommendService.uploadPost(userEmail, request);
        return Response.success(response);
    }

    /**
     * 등록된 추천글을 수정합니다.
     *
     * @param authentication header의 token
     * @param request 수정할 추천글 정보
     * @param recommendNo 수정할 추천글 고유 번호
     * @return 수정된 추천글 내용
     */
    @PutMapping("/{recommendNo}")
    public Response<RecommendModifyResponse> modifyRecommendPost(final Authentication authentication,
                                                                 @RequestBody @Valid final RecommendModifyRequest request,
                                                                 @PathVariable final Long recommendNo) {
        String userEmail = authentication.getName();
        log.info("user_email = {}, recommend_modify_request = {}", userEmail, request);

        RecommendModifyResponse response = recommendService.modifyPost(userEmail, recommendNo, request);
        return Response.success(response);
    }

    /**
     * 등록된 추천 글을 삭제합니다.
     * @param authentication header의 token
     * @param recommendNo 삭제할 추천글 고유번호
     * @return 삭제 결과
     */
    @DeleteMapping("/{recommendNo}")
    public Response<RecommendDeleteResponse> deleteRecommendPost(final Authentication authentication,
                                                                 @PathVariable final Long recommendNo) {
        String userEmail = authentication.getName();
        log.info("user email = {}, recommend_no = {}", userEmail, recommendNo);

        recommendService.deletePost(userEmail, recommendNo);
        return Response.success(new RecommendDeleteResponse(recommendNo, "추천 글이 삭제 되었습니다."));
    }

    /**
     * 추천글의 정보를 받아옵니다.
     * 
     * @param recommendNo 정보를 받아올 추천글 고유번호
     * @return 추천글의 정보를 반환합니다.
     */
    @GetMapping("/{recommendNo}")
    public Response<RecommendReadResponse> ReadRecommendPost(@PathVariable final Long recommendNo) {
        log.info("recommend_no = {}", recommendNo);

        RecommendReadResponse response = recommendService.readPost(recommendNo);
        return Response.success(response);
    }
}
