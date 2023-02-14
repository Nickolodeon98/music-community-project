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

  @PostMapping("/followCancel/{nickName}")
  public String followCanel(@PathVariable String nickName, HttpServletRequest request) {

    HttpSession session = request.getSession(false);
    MemberLoginResponse loginResponse = (MemberLoginResponse) session.getAttribute("loginUser");
    followService.followCancel(loginResponse.getNickName(), nickName);
    return "redirect:/member/followings";
  }

  @PostMapping("/follow/{nickName}")
  public String follow(@PathVariable String nickName, HttpServletRequest request) {

    HttpSession session = request.getSession(false);
    MemberLoginResponse loginResponse = (MemberLoginResponse) session.getAttribute("loginUser");
    followService.follow(loginResponse.getNickName(), nickName);
    return "redirect:/member/followers";
  }
}
