package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.follow.FollowMyFeedResponse;
import com.content_i_like.domain.dto.follow.FollowResponse;
import com.content_i_like.domain.dto.member.ChangePwRequest;
import com.content_i_like.domain.dto.member.MemberCommentResponse;
import com.content_i_like.domain.dto.member.MemberFindRequest;
import com.content_i_like.domain.dto.member.MemberJoinRequest;
import com.content_i_like.domain.dto.member.MemberJoinResponse;
import com.content_i_like.domain.dto.member.MemberLoginRequest;
import com.content_i_like.domain.dto.member.MemberLoginResponse;
import com.content_i_like.domain.dto.member.MemberModifyRequest;
import com.content_i_like.domain.dto.member.MemberPointResponse;
import com.content_i_like.domain.dto.member.MemberRecommendResponse;
import com.content_i_like.domain.dto.member.MemberResponse;
import com.content_i_like.domain.dto.recommend.RecommendListResponse;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

  private final MemberService memberService;

  @GetMapping("/login")
  public String joinForm(Model model) {
    model.addAttribute("request", new MemberLoginRequest());
    return "pages/member/login";
  }

  @PostMapping("/login")
  public String login(
      @Valid @ModelAttribute("memberLoginRequest") MemberLoginRequest memberLoginRequest,
      BindingResult bindingResult,
      HttpServletRequest request, Model model) {

    if (bindingResult.hasErrors()) {
      return "pages/member/login";
    }
    MemberLoginResponse response = memberService.login(memberLoginRequest);

    HttpSession session = request.getSession();   //세션이 있으면 있는 세션 반환, 없으면 신규 세션
    session.setAttribute("loginUser", response);
    log.info("로그인 완료");
    return "redirect:/";
  }

  @GetMapping("/session-info")
  public String sessionInfo(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return "세션이 없습니다.";
    }
    // 세션 id와 저장된 객체 정보 출력
    System.out.println(session.getId() + ", " + session.getAttribute("loginMember"));

    //세션 데이터 출력
    session.getAttributeNames().asIterator()
        .forEachRemaining(
            name -> log.info("session name={}, value={}", name, session.getAttribute(name)));

    log.info("sessionId={}", session.getId());
    log.info("getMaxInactiveInterval={}", session.getMaxInactiveInterval());
    log.info("creationTime={}", new Date(session.getCreationTime()));
    log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime()));
    log.info("isNew={}", session.isNew());

    return "세션 출력";

  }

  @PostMapping("/join")
  public Response<MemberJoinResponse> join(@RequestBody @Valid final MemberJoinRequest request) {
    MemberJoinResponse response = memberService.join(request);
    return Response.success(response);
  }


  @PostMapping("/passwd/find_pw")
  public Response<String> findPw(@RequestBody @Valid final MemberFindRequest request) {
    String message = memberService.findPwByEmail(request);
    return Response.success(message);
  }

  @PostMapping("/passwd/change")
  public Response<String> changePw(@RequestBody @Valid final ChangePwRequest request,
      final Authentication authentication) {
    return Response.success(memberService.changePw(request, authentication.getName()));
  }

  @GetMapping("/my")
  public Response<MemberResponse> getMyInfo(final Authentication authentication) {
    MemberResponse memberResponse = memberService.getMyInfo(authentication.getName());
    return Response.success(memberResponse);
  }

  @GetMapping("/my/point")
  public Response<MemberPointResponse> getMyPoint(final Authentication authentication) {
    MemberPointResponse memberPointResponse = memberService.getMyPoint(authentication.getName());
    return Response.success(memberPointResponse);
  }

  @PutMapping("/my")
  public Response<MemberResponse> modifyMyInfo(
      @RequestPart(value = "dto") @Valid final MemberModifyRequest request,
      @RequestPart(value = "file", required = false) MultipartFile file,
      final Authentication authentication) throws IOException {
    MemberResponse memberResponse = memberService
        .modifyMyInfo(request, file, authentication.getName());
    return Response.success(memberResponse);
  }

  @PutMapping("/my/profileImg")
  public Response<String> updateProfileImg(@RequestPart(value = "file") MultipartFile file,
      Authentication authentication) throws IOException {
    String url = memberService.uploadProfileImg(authentication.getName(), file);
    return Response.success(url);
  }

  @GetMapping("/recommends")
  public String getMyRecommends(HttpServletRequest request, Model model, Pageable pageable) {

    HttpSession session = request.getSession(false);
    MemberLoginResponse loginResponse = (MemberLoginResponse) session.getAttribute("loginUser");
    MemberRecommendResponse recommends = memberService.getMyRecommendsByNickName(
        loginResponse.getNickName(), pageable);

    model.addAttribute("recommendsResponse", recommends);

    return "pages/member/myFeed-recommends";
  }

  @GetMapping("/comments")
  public String getMyComments(HttpServletRequest request, Model model, Pageable pageable) {
    HttpSession session = request.getSession(false);
    MemberLoginResponse loginResponse = (MemberLoginResponse) session.getAttribute("loginUser");
    MemberCommentResponse comments = memberService.getMyCommentsByNickName(
        loginResponse.getNickName(), pageable);

    model.addAttribute("comments", comments);

    return "pages/member/myFeed-comments";
  }

  @GetMapping("/followers")
  public String getMyFollowers(HttpServletRequest request, Model model, Pageable pageable) {
    HttpSession session = request.getSession(false);
    MemberLoginResponse loginResponse = (MemberLoginResponse) session.getAttribute("loginUser");
    FollowMyFeedResponse myFollowersByNickName = memberService.getMyFollowersByNickName(
        loginResponse.getNickName(), pageable);

    model.addAttribute("followers", myFollowersByNickName);
    return "pages/member/myFeed-followers";
  }

  @GetMapping("/followings")
  public String getMyFollowings(HttpServletRequest request, Model model, Pageable pageable) {
    HttpSession session = request.getSession(false);
    MemberLoginResponse loginResponse = (MemberLoginResponse) session.getAttribute("loginUser");
    FollowMyFeedResponse myFollowersByNickName = memberService.getMyFollowingsByNickName(
        loginResponse.getNickName(), pageable);

    model.addAttribute("followers", myFollowersByNickName);
    return "pages/member/myFeed-followings";
  }
}