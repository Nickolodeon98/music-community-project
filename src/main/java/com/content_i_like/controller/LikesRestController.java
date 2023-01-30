package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.service.LikesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recommends")
@Slf4j
public class LikesRestController {

  private final LikesService likesService;

  /**
   * 좋아요를 활성화 또는 비활성화 합니다.
   *
   * @param authentication header에서 받아오는 token.
   * @param recommendNo    요청을 받는 recommend글의 no.
   * @return 좋아요 활성 또는 비활성 상태의 결과 반환
   */
  @PostMapping("/{recommendNo}/likes")
  public Response<String> changeLikesStatus(final Authentication authentication,
      @PathVariable final Long recommendNo) {
    log.info("authentication = {}, recommendNo = {}", authentication, recommendNo);

    String userName = authentication.getName();
    return Response.success(likesService.changeLikesStatus(userName, recommendNo));
  }

  /**
   * 추천 글에 활성화된 좋아요 개수를 반환합니다.
   *
   * @param recommendNo 좋아요 개수를 확인할 추천 게시글
   * @return 좋아요 개수를 숫자로 반환합니다.
   */
  @GetMapping("/{recommendNo}/likes")
  public Response<Integer> returnNumberLikes(@PathVariable final Long recommendNo) {
    return Response.success(likesService.countNumberLikes(recommendNo));
  }
}
