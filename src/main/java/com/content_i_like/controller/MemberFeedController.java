package com.content_i_like.controller;

import com.content_i_like.domain.dto.follow.FollowMyFeedResponse;
import com.content_i_like.domain.dto.member.MemberCommentResponse;
import com.content_i_like.domain.dto.member.MemberLoginResponse;
import com.content_i_like.domain.dto.member.MemberRecommendResponse;
import com.content_i_like.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberFeedController {

  private final MemberService memberService;

  @GetMapping("/recommends/feeds/{memberNo}")
  public String getMyRecommends(@PathVariable Long memberNo, Model model, Pageable pageable) {
    MemberRecommendResponse recommends = memberService.getMyRecommendsByNickName(
        memberService.getNickNameByNo(memberNo), pageable);

    model.addAttribute("recommendsResponse", recommends);
    model.addAttribute("url", "/feeds/"+memberNo);

    return "pages/member/myFeed-recommends";
  }

  @GetMapping("/comments/feeds/{memberNo}")
  public String getMyComments(@PathVariable Long memberNo, Model model, Pageable pageable) {
    MemberCommentResponse comments = memberService.getMyCommentsByNickName(
        memberService.getNickNameByNo(memberNo), pageable);

    model.addAttribute("comments", comments);
    model.addAttribute("url", "/feeds/"+memberNo);

    return "pages/member/myFeed-comments";
  }

  @GetMapping("/followers/feeds/{memberNo}")
  public String getMyFollowers(@PathVariable Long memberNo, Model model, Pageable pageable) {
    FollowMyFeedResponse myFollowersByNickName = memberService.getMyFollowersByNickName(
        memberService.getNickNameByNo(memberNo), pageable);

    model.addAttribute("followers", myFollowersByNickName);
    model.addAttribute("url", "/feeds/"+memberNo);

    return "pages/member/myFeed-followers";
  }

  @GetMapping("/followings/feeds/{memberNo}")
  public String getMyFollowings(@PathVariable Long memberNo, Model model, Pageable pageable) {
    FollowMyFeedResponse myFollowersByNickName = memberService.getMyFollowingsByNickName(
        memberService.getNickNameByNo(memberNo), pageable);

    model.addAttribute("followers", myFollowersByNickName);
    model.addAttribute("url", "/feeds/"+memberNo);

    return "pages/member/myFeed-followings";
  }

}
