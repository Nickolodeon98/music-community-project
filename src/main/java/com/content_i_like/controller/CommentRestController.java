package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.comment.CommentDeleteResponse;
import com.content_i_like.domain.dto.comment.CommentModifyRequest;
import com.content_i_like.domain.dto.comment.CommentRequest;
import com.content_i_like.domain.dto.comment.CommentResponse;
import com.content_i_like.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recommends")
@RequiredArgsConstructor
@Slf4j
public class CommentRestController {

    private final CommentService commentService;

    @PostMapping("/{recommendNo}/comments")
    public Response<CommentResponse> writeRecommendComment(Authentication authentication,
                                                           @RequestBody CommentRequest request,
                                                           @PathVariable Long recommendNo) {
        String userEmail = authentication.getName();
        log.info("user_email = {}, request = {}, recommend_no = {}", authentication, request.getCommentContent(), recommendNo);

        CommentResponse response = commentService.writeComment(userEmail, request, recommendNo);
        return Response.success(response);
    }

    @PutMapping("/{recommendNo}/comments/{commentNo}")
    public Response<CommentResponse> writeRecommendComment(Authentication authentication,
                                                           @RequestBody CommentModifyRequest request,
                                                           @PathVariable Long recommendNo,
                                                           @PathVariable Long commentNo) {
        String userEmail = authentication.getName();
        log.info("user_email = {}, request = {}, recommend_no = {}, commentNo = {}", authentication, request.getCommentContent(), recommendNo, commentNo);

        CommentResponse response = commentService.modifyComment(userEmail, request, recommendNo, commentNo);
        return Response.success(response);
    }

    @DeleteMapping("/{recommendNo}/comments/{commentNo}")
    public Response<CommentDeleteResponse> deleteRecommendComment(Authentication authentication,
                                                           @PathVariable Long recommendNo,
                                                           @PathVariable Long commentNo) {
        String userEmail = authentication.getName();
        log.info("user_email = {}, recommend_no = {}, commentNo = {}", authentication, recommendNo, commentNo);

        commentService.deleteComment(userEmail, recommendNo, commentNo);
        return Response.success(new CommentDeleteResponse(commentNo, recommendNo, "댓글이 삭제 되었습니다."));
    }
}
