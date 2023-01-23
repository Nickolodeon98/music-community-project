package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.comment.*;
import com.content_i_like.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recommends")
@RequiredArgsConstructor
@Slf4j
public class CommentRestController {

    private final CommentService commentService;

    @PostMapping("/{recommendNo}/comments")
    public Response<CommentResponse> writeRecommendComment(final Authentication authentication,
                                                           @RequestBody @Valid final CommentRequest request,
                                                           @PathVariable final Long recommendNo) {
        String userEmail = authentication.getName();
        log.info("user_email = {}, request = {}, recommend_no = {}", authentication, request.getCommentContent(), recommendNo);

        CommentResponse response = commentService.writeComment(userEmail, request, recommendNo);
        return Response.success(response);
    }

    @PutMapping("/{recommendNo}/comments/{commentNo}")
    public Response<CommentResponse> writeRecommendComment(final Authentication authentication,
                                                           @RequestBody @Valid CommentModifyRequest request,
                                                           @PathVariable final Long recommendNo,
                                                           @PathVariable final Long commentNo) {
        String userEmail = authentication.getName();
        log.info("user_email = {}, request = {}, recommend_no = {}, commentNo = {}", authentication, request.getCommentContent(), recommendNo, commentNo);

        CommentResponse response = commentService.modifyComment(userEmail, request, recommendNo, commentNo);
        return Response.success(response);
    }

    @DeleteMapping("/{recommendNo}/comments/{commentNo}")
    public Response<CommentDeleteResponse> deleteRecommendComment(final Authentication authentication,
                                                                  @PathVariable final Long recommendNo,
                                                                  @PathVariable final Long commentNo) {
        String userEmail = authentication.getName();
        log.info("user_email = {}, recommend_no = {}, commentNo = {}", authentication, recommendNo, commentNo);

        commentService.deleteComment(userEmail, recommendNo, commentNo);
        return Response.success(new CommentDeleteResponse(commentNo, recommendNo, "댓글이 삭제 되었습니다."));
    }

    @GetMapping("/{recommendNo}/comments/{commentNo}")
    public Response<CommentReadResponse> getRecommendComent(@PathVariable final Long recommendNo,
                                                            @PathVariable final Long commentNo) {
        log.info("recommend_no = {}, commentNo = {}", recommendNo, commentNo);
        CommentReadResponse response = commentService.getReadComment(recommendNo,commentNo);
        return Response.success(response);
    }

    @GetMapping("/{recommendNo}/comments")
    public Response<Page<CommentReadResponse>> getRecommendAllComment(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable,
                                                          @PathVariable final Long recommendNo) {
        log.info("pageable = {}, recommendsNo = {}", pageable, recommendNo);
        return Response.success(commentService.getReadAllComment(pageable, recommendNo));
    }
}