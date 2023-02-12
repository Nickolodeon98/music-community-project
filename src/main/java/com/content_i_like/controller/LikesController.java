package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.member.MemberLoginResponse;
import com.content_i_like.repository.MemberRepository;
import com.content_i_like.service.LikesService;
import com.content_i_like.service.MemberService;
import com.content_i_like.service.ValidateService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recommends")
@Slf4j
public class LikesController {

  private final LikesService likesService;
  private final MemberService memberService;
  private final ValidateService validateService;

  /**
   * 좋아요를 활성화 또는 비활성화 합니다.
   *
   * @param recommendNo 요청을 받는 recommend글의 no.
   * @return 좋아요 활성 또는 비활성 상태의 결과 반환
   */
  @PostMapping("/{recommendNo}/likes")
  @ResponseBody
  public Long changeLikesStatus(@PathVariable final Long recommendNo,
      HttpServletRequest servletRequest) {
    HttpSession session = servletRequest.getSession(false);
    MemberLoginResponse loginResponse = (MemberLoginResponse) session.getAttribute("loginUser");
    String memberEmail = validateService.validateMemberByMemberNo(loginResponse.getMemberNo()).getEmail();
    likesService.changeLikesStatus(memberEmail, recommendNo);

    System.out.println(likesService.countNumberLikes(recommendNo));
    return likesService.countNumberLikes(recommendNo);
  }

  /**
   * 추천 글에 활성화된 좋아요 개수를 반환합니다.
   *
   * @param recommendNo 좋아요 개수를 확인할 추천 게시글
   * @return 좋아요 개수를 숫자로 반환합니다.
   */
  @GetMapping("/{recommendNo}/likes")
  public Response<Long> returnNumberLikes(@PathVariable final Long recommendNo) {
    return Response.success(likesService.countNumberLikes(recommendNo));
  }
}
