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
import com.content_i_like.domain.enums.GenderEnum;
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

  @ModelAttribute("genderEnums")
  public GenderEnum[] genderEnum() {
    return GenderEnum.values();
  }

  @GetMapping("/login")
  public String loginForm(Model model) {
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

  @GetMapping("/join")
  public String joinForm(Model model) {
    model.addAttribute("request", new MemberJoinRequest());
    return "pages/member/join";
  }

  @PostMapping("/join")
  public String join(@Valid @ModelAttribute("memberJoinRequest") MemberJoinRequest request) {
    MemberJoinResponse response = memberService.join(request);
    return "redirect:/member/login";
  }

  @GetMapping("/passwd/find_pw")
  public String findPwForm(HttpServletRequest httpRequest, Model model) {
    if (httpRequest.getSession(false) != null) {
      return "redirect:/";
    }
    model.addAttribute("request", new MemberFindRequest());
    return "pages/member/find-pw";
  }

  @PostMapping("/passwd/find_pw")
  public String findPw(@Valid @ModelAttribute("memberFindRequest") MemberFindRequest request) {
    String message = memberService.findPwByEmail(request);
    return "redirect:/member/login";
  }

  @GetMapping("/passwd/change")
  public String changePw(HttpServletRequest httpRequest, Model model) {
    if (httpRequest.getSession(false) == null) {
      return "redirect:/member/login";
    }
    model.addAttribute("request", new ChangePwRequest());
    return "pages/member/change-pw";
  }

  @PostMapping("/passwd/change")
  public String changePw(HttpServletRequest httpRequest,
      @Valid @ModelAttribute("changePwRequest") ChangePwRequest request) {
    HttpSession session = httpRequest.getSession(false);
    MemberLoginResponse loginResponse = (MemberLoginResponse) session.getAttribute("loginUser");
    log.info("세션 정보: {} and {}", loginResponse.getMemberNo(), loginResponse.getNickName());
    String email = memberService.getEmailByNo(loginResponse);
    memberService.changePw(request, email);
    return "redirect:/";
  }

  @GetMapping("/my")
  public String getMyInfo(HttpServletRequest httpRequest, Model model) {
    HttpSession session = httpRequest.getSession(false);
    if (session == null) {
      return "redirect:/member/login";
    }
    MemberLoginResponse loginResponse = (MemberLoginResponse) session.getAttribute("loginUser");

    String email = memberService.getEmailByNo(loginResponse);
    model.addAttribute("member", memberService.getMyInfo(email));
    return "pages/member/my-profile";
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
}