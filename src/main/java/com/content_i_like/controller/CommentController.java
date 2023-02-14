package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.comment.CommentDeleteResponse;
import com.content_i_like.domain.dto.comment.CommentModifyRequest;
import com.content_i_like.domain.dto.comment.CommentReadResponse;
import com.content_i_like.domain.dto.comment.CommentRequest;
import com.content_i_like.domain.dto.comment.CommentResponse;
import com.content_i_like.domain.dto.comment.SuperChatReadResponse;
import com.content_i_like.domain.dto.member.MemberLoginResponse;
import com.content_i_like.service.CommentService;
import com.content_i_like.service.ValidateService;
import com.content_i_like.utils.GsonUtils;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/recommends")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

  private final CommentService commentService;
  private final ValidateService validateService;

  /**
   * 추천글에 댓글을 작성합니다.
   *
   * @param request        작성하는 댓글 정보
   * @param recommendNo    댓글을 작성할 추천글의 고유 번호
   * @return 작성된 댓글 내용
   */
  @PostMapping("/{recommendNo}/comments")
  @ResponseBody
  public Map<String, Object> writeRecommendComment(@SessionAttribute(name = "loginUser", required = true) MemberLoginResponse loginMember,
      @RequestBody @Valid final CommentRequest request,
      @PathVariable final Long recommendNo) {
    Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

    String userEmail = validateService.validateMemberByMemberNo(loginMember.getMemberNo()).getEmail();
    log.info("user_email = {}, request = {}, recommend_no = {}", userEmail,
        request.getCommentContent(), recommendNo);

    CommentResponse response = commentService.writeComment(userEmail, request, recommendNo);

    Page<SuperChatReadResponse> superChats = commentService.gerSuperChatUser(pageable, recommendNo);
    if (superChats != null && superChats.getContent().size() > 5) {
      superChats = new PageImpl<>(superChats.getContent().subList(0, 5), superChats.getPageable(), 5);
    }

    log.info("superchat = {}", superChats.getContent().get(0).getMemberNickname());
    List<CommentReadResponse> commentList = commentService.getReadAllComment(recommendNo);

    Map<String, Object> map = new HashMap<>();
    map.put("comments", commentList);
    map.put("superchats", superChats.getContent());
    return map;
  }

  /**
   * 등록된 댓글을 수정합니다.
   *
   * @param authentication header의 token
   * @param request        수정할 댓글 정보
   * @param recommendNo    수정할 댓글이 존재하는 추천글 고유번호
   * @param commentNo      수정할 댓글 고유번호
   * @return 수정된 댓글 내용
   */
  @PutMapping("/{recommendNo}/comments/{commentNo}")
  public Response<CommentResponse> modifyRecommendComment(final Authentication authentication,
      @RequestBody @Valid CommentModifyRequest request,
      @PathVariable final Long recommendNo,
      @PathVariable final Long commentNo) {
    String userEmail = authentication.getName();
    log.info("user_email = {}, request = {}, recommend_no = {}, commentNo = {}", authentication,
        request.getCommentContent(), recommendNo, commentNo);

    CommentResponse response = commentService.modifyComment(userEmail, request, recommendNo,
        commentNo);
    return Response.success(response);
  }

  /**
   * 작성된 댓글을 삭제합니다.
   *
   * @param authentication header의 token
   * @param recommendNo    삭제할 댓글이 존재하는 추천글 고유번호
   * @param commentNo      삭제할 댓글의 고유번호
   * @return 삭제 결과
   */
  @DeleteMapping("/{recommendNo}/comments/{commentNo}")
  public Response<CommentDeleteResponse> deleteRecommendComment(final Authentication authentication,
      @PathVariable final Long recommendNo,
      @PathVariable final Long commentNo) {
    String userEmail = authentication.getName();
    log.info("user_email = {}, recommend_no = {}, commentNo = {}", authentication, recommendNo,
        commentNo);

    commentService.deleteComment(userEmail, recommendNo, commentNo);
    return Response.success(new CommentDeleteResponse(commentNo, recommendNo, "댓글이 삭제 되었습니다."));
  }

  /**
   * 추천글에 작성된 특정 댓글 정보를 불러옵니다.
   *
   * @param recommendNo 가져올 댓글이 존재하는 추천글 고유번호
   * @param commentNo   정보를 반환할 댓글 고유번호
   * @return 댓글 정보
   */
  @GetMapping("/{recommendNo}/comments/{commentNo}")
  public Response<CommentReadResponse> getRecommendComment(@PathVariable final Long recommendNo,
      @PathVariable final Long commentNo) {
    log.info("recommend_no = {}, commentNo = {}", recommendNo, commentNo);
    CommentReadResponse response = commentService.getReadComment(recommendNo, commentNo);
    return Response.success(response);
  }


  @GetMapping("/{recommendNo}/comments")
  @ResponseBody
  public String getRecommendComments(Model model, @PathVariable final Long recommendNo) {
    List<CommentReadResponse> commentList = commentService.getReadAllComment(recommendNo);
    model.addAttribute("comments", commentList);
    return "pages/recommend/comment-list";
  }
}