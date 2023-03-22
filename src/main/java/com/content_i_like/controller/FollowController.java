package com.content_i_like.controller;

import com.content_i_like.domain.dto.member.MemberLoginResponse;
import com.content_i_like.service.FollowService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {

  private final FollowService followService;

  /**
   * 팔로우를 취소합니다
   *
   * @param nickName  팔로우 취소하고자 하는 대상의 닉네임
   * @return 팔로윙 목록
   */
  @PostMapping("/followCancel/{nickName}")
  public String followCanel(@PathVariable String nickName, HttpServletRequest request) {

    HttpSession session = request.getSession(false);
    MemberLoginResponse loginResponse = (MemberLoginResponse) session.getAttribute("loginUser");
    followService.followCancel(loginResponse.getNickName(), nickName);
    return "redirect:/member/followings";
  }

  /**
   * 팔로우에 추가합니다
   *
   * @param nickName  팔로우 하고자 하는 대상의 닉네임
   * @return 팔로워 목록
   */
  @PostMapping("/follow/{nickName}")
  public String follow(@PathVariable String nickName, HttpServletRequest request) {

    HttpSession session = request.getSession(false);
    MemberLoginResponse loginResponse = (MemberLoginResponse) session.getAttribute("loginUser");
    followService.follow(loginResponse.getNickName(), nickName);
    return "redirect:/member/followers";
  }
}
