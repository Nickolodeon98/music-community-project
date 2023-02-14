package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.comment.CommentReadResponse;
import com.content_i_like.domain.dto.comment.SuperChatReadResponse;
import com.content_i_like.domain.dto.member.LoginUserResponse;
import com.content_i_like.domain.dto.member.MemberLoginResponse;
import com.content_i_like.domain.dto.recommend.RecommendDeleteResponse;
import com.content_i_like.domain.dto.recommend.RecommendListResponse;
import com.content_i_like.domain.dto.recommend.RecommendModifyRequest;
import com.content_i_like.domain.dto.recommend.RecommendModifyResponse;
import com.content_i_like.domain.dto.recommend.RecommendPostRequest;
import com.content_i_like.domain.dto.recommend.RecommendPostResponse;
import com.content_i_like.domain.dto.recommend.RecommendReadResponse;
import com.content_i_like.domain.entity.Recommend;
import com.content_i_like.service.CommentService;
import com.content_i_like.service.MemberService;
import com.content_i_like.service.PointService;
import com.content_i_like.service.RecommendService;
import com.content_i_like.service.ValidateService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/recommends")
@RequiredArgsConstructor
@Slf4j
public class RecommendController {

  private final RecommendService recommendService;
  private final CommentService commentService;
  private final MemberService memberService;
  private final ValidateService validateService;
  private final PointService pointService;


  @GetMapping("/writeForm")
  public String recommendWriteForm(
      @SessionAttribute(name = "loginUser", required = true) MemberLoginResponse loginMember,
      Model model) {
    model.addAttribute("request", new RecommendPostRequest());
    return "pages/recommend/recommend-post";
  }

  /**
   * 추천글의 정보를 받아옵니다.
   *
   * @param recommendNo 정보를 받아올 추천글 고유번호
   * @return 추천글의 정보를 반환합니다.
   */
  @GetMapping("/{recommendNo}")
  public String ReadRecommendPost(@PathVariable final Long recommendNo,
      HttpServletRequest servletRequest,
      Model model) {
    log.info("recommend_no = {}", recommendNo);
    Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
    RecommendReadResponse response = recommendService.readPost(recommendNo);
    Page<CommentReadResponse> comments = commentService.getReadAllComment(pageable, recommendNo);
    Page<SuperChatReadResponse> superChats = commentService.gerSuperChatUser(pageable, recommendNo);
    if (superChats != null && superChats.getContent().size() > 5) {
      superChats = new PageImpl<>(superChats.getContent().subList(0, 5), superChats.getPageable(),
          5);
    }
    HttpSession session = servletRequest.getSession(false);

    if (session != null && session.getAttribute("loginUser") != null) {
      MemberLoginResponse loginResponse = (MemberLoginResponse) session.getAttribute("loginUser");
      LoginUserResponse loginUserResponse = LoginUserResponse.of(
          validateService.validateMemberByMemberNo(
              loginResponse.getMemberNo()));
      Long loginUserPoint = pointService.calculatePoint(validateService.validateMemberByMemberNo(
          loginResponse.getMemberNo()));
      boolean checkComment = commentService.checkWriteComment(loginResponse.getMemberNo(),
          recommendNo);
      log.info("댓글 달았니? = {}", checkComment);
      model.addAttribute("login", loginUserResponse);
      model.addAttribute("loginUserPoint", loginUserPoint);
      model.addAttribute("checkComment", checkComment);
    }

    model.addAttribute("post", response);
    model.addAttribute("comments", comments);
    model.addAttribute("superchats", superChats);

    return "pages/recommend/recommend-read";
  }

  @PostMapping()
  public String uploadRecommendPost(/*final HttpSession session,*/
      @ModelAttribute("request") @Valid final RecommendPostRequest request,
      HttpServletRequest servletRequest) throws IOException {
    HttpSession session = servletRequest.getSession(false);
    String memberEmail = getLoginInfo(session);
    RecommendPostResponse response = recommendService.uploadPost(memberEmail, request);
    return "redirect:/";
  }


  @GetMapping("/{recommendNo}/modifyForm")
  public String recommendModifyForm(
      @SessionAttribute(name = "loginUser", required = true) MemberLoginResponse loginMember,
      HttpServletRequest request,
      Model model,
      @PathVariable("recommendNo") Long recommendNo) {

    Recommend post = recommendService.findPostById(recommendNo);
    String hashtags = recommendService.getHashtagsToString(recommendNo);

    if (!Objects.equals(loginMember.getMemberNo(), post.getMember().getMemberNo())) {
      return "redirect:/error";
    }

    model.addAttribute("request", new RecommendModifyRequest());
    model.addAttribute("post", post);
    model.addAttribute("hashtags", hashtags);
    return "pages/recommend/recommend-modify";
  }


  @PostMapping("/{recommendNo}/update")
  public String modifyRecommendPost(
      @ModelAttribute("request") @Valid final RecommendModifyRequest request,
      @PathVariable final Long recommendNo,
      HttpServletRequest servletRequest) throws IOException {
    HttpSession session = servletRequest.getSession(false);
    String memberEmail = getLoginInfo(session);

    RecommendModifyResponse response = recommendService.modifyPost(memberEmail, recommendNo,
        request);
    return "redirect:/recommends/" + recommendNo;
  }

  @PostMapping("/{recommendNo}/delete")
  public String deleteRecommendPost(
      @PathVariable final Long recommendNo,
      HttpServletRequest servletRequest) {
    HttpSession session = servletRequest.getSession(false);
    String memberEmail = getLoginInfo(session);
    log.info("memberEmail = {}, recommend_no = {}", memberEmail, recommendNo);

    recommendService.deletePost(memberEmail, recommendNo);
    return "redirect:/";
  }

  private String getLoginInfo(HttpSession session) {
    MemberLoginResponse loginResponse = (MemberLoginResponse) session.getAttribute("loginUser");
    return validateService.validateMemberByMemberNo(loginResponse.getMemberNo()).getEmail();
  }

  //////////// REST Controller //////////
  /*

   */
/*
   * 등록된 추천글을 수정합니다.
   *
   * @param authentication header의 token
   * @param request        수정할 추천글 정보
   * @param recommendNo    수정할 추천글 고유 번호
   * @return 수정된 추천글 내용
   /*

  @PostMapping("/{recommendNo}/update")
  public Response<RecommendModifyResponse> modifyRecommendPost(final Authentication authentication,
      @RequestPart(required = false, name = "image") MultipartFile image,
      @RequestPart(name = "request") @Valid final RecommendModifyRequest request,
      @RequestPart(required = false, name = "hashtag1") final String hashtag1,
      @RequestPart(required = false, name = "hashtag2") final String hashtag2,
      @RequestPart(required = false, name = "hashtag3") final String hashtag3,
      @PathVariable final Long recommendNo) throws IOException {
    String userEmail = authentication.getName();
    log.info("user_email = {}, recommend_modify_request = {}", userEmail, request);

    List<String> hashtags = Stream.of(hashtag1, hashtag2, hashtag3)
        .distinct()
        .filter(Objects::nonNull).toList();

    RecommendModifyResponse response = recommendService.modifyPost(userEmail, recommendNo, request,
        hashtags);
    return Response.success(response);
  }
*/

  /**
   * 등록된 추천 글을 삭제합니다.
   *
   * @param authentication header의 token
   * @param recommendNo    삭제할 추천글 고유번호
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


  @GetMapping// local/api/v1/recommend?sort=recommendNo; "게시글순" name = sort, value = "recommendNo"
  public Response<Page<RecommendListResponse>> ReadRecommendPost(
      @RequestParam(required = false, defaultValue = "recommendTitle") String sort) {
    Pageable pageable = PageRequest.of(0, 20, Sort.by(sort).ascending());

    log.info("recommend_no = {}", sort);

    Page<RecommendListResponse> response = recommendService.getPostList(pageable, "createdAt");
    return Response.success(response);
  }
}
