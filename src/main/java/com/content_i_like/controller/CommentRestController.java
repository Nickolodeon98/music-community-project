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

    /**
     * 추천글에 댓글을 작성합니다.
     * 
     * @param authentication header의 token.
     * @param request 작성하는 댓글 정보
     * @param recommendNo 댓글을 작성할 추천글의 고유 번호
     * @return 작성된 댓글 내용
     */
    @PostMapping("/{recommendNo}/comments")
    public Response<CommentResponse> writeRecommendComment(final Authentication authentication,
                                                           @RequestBody @Valid final CommentRequest request,
                                                           @PathVariable final Long recommendNo) {
        String userEmail = authentication.getName();
        log.info("user_email = {}, request = {}, recommend_no = {}", authentication, request.getCommentContent(), recommendNo);

        CommentResponse response = commentService.writeComment(userEmail, request, recommendNo);
        return Response.success(response);
    }

    /**
     * 등록된 댓글을 수정합니다.
     * @param authentication header의 token
     * @param request 수정할 댓글 정보
     * @param recommendNo 수정할 댓글이 존재하는 추천글 고유번호
     * @param commentNo 수정할 댓글 고유번호
     * @return 수정된 댓글 내용
     */
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

    /**
     * 작성된 댓글을 삭제합니다.
     * @param authentication header의 token
     * @param recommendNo 삭제할 댓글이 존재하는 추천글 고유번호
     * @param commentNo 삭제할 댓글의 고유번호
     * @return 삭제 결과
     */
    @DeleteMapping("/{recommendNo}/comments/{commentNo}")
    public Response<CommentDeleteResponse> deleteRecommendComment(final Authentication authentication,
                                                                  @PathVariable final Long recommendNo,
                                                                  @PathVariable final Long commentNo) {
        String userEmail = authentication.getName();
        log.info("user_email = {}, recommend_no = {}, commentNo = {}", authentication, recommendNo, commentNo);

        commentService.deleteComment(userEmail, recommendNo, commentNo);
        return Response.success(new CommentDeleteResponse(commentNo, recommendNo, "댓글이 삭제 되었습니다."));
    }

    /**
     * 추천글에 작성된 특정 댓글 정보를 불러옵니다.
     * 
     * @param recommendNo 가져올 댓글이 존재하는 추천글 고유번호
     * @param commentNo 정보를 반환할 댓글 고유번호
     * @return 댓글 정보
     */
    @GetMapping("/{recommendNo}/comments/{commentNo}")
    public Response<CommentReadResponse> getRecommendComment(@PathVariable final Long recommendNo,
                                                            @PathVariable final Long commentNo) {
        log.info("recommend_no = {}, commentNo = {}", recommendNo, commentNo);
        CommentReadResponse response = commentService.getReadComment(recommendNo,commentNo);
        return Response.success(response);
    }

    /**
     * 추천글에 작성된 모든 댓글 정보를 불러옵니다.
     * @param pageable 페이징 정보
     * @param recommendNo 댓글을 가져올 추천글 고유번호
     * @return 해당 추천글의 모든 댓글 정보
     */
    @GetMapping("/{recommendNo}/comments")
    public Response<Page<CommentReadResponse>> getRecommendAllComment(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable,
                                                          @PathVariable final Long recommendNo) {
        log.info("pageable = {}, recommendsNo = {}", pageable, recommendNo);
        return Response.success(commentService.getReadAllComment(pageable, recommendNo));
    }
}