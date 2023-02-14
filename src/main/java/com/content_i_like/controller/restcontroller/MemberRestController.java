package com.content_i_like.controller.restcontroller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.member.*;
import com.content_i_like.domain.dto.recommend.RecommendListResponse;
import com.content_i_like.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberRestController {

  private final MemberService memberService;

  @PostMapping("/join")
  public Response<MemberJoinResponse> join(@RequestBody @Valid final MemberJoinRequest request) {
    MemberJoinResponse response = memberService.join(request);
    return Response.success(response);
  }

  @PostMapping("/login")
  public Response<MemberLoginResponse> login(@RequestBody @Valid final MemberLoginRequest request) {
    MemberLoginResponse response = memberService.login(request);
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
  public Response<MemberPointResponse> getMyPoint(final Authentication authentication, Pageable pageable) {
    MemberPointResponse memberPointResponse = memberService.getMyPoint(authentication.getName(), pageable);
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

  /**
   * 내가 등록한 recommends를 불러옵니다.
   *
   * @return 작성한 recommends 목록
   */
  @GetMapping("/recommends")
  public Response<Page<RecommendListResponse>> getMyRecommends(Authentication authentication,
      @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable) {
    return Response.success(memberService.getMyRecommends(authentication.getName(), pageable));
  }

  /**
   * 내가 등록한 recommends와 다른 정보를 통합해서 불러옵니다.
   *
   * @return 작성 게시글 수, 팔로워 수, 팔로윙 수, 작성한 recommends 목록
   */
  @GetMapping("/recommends/integrated")
  public Response<MemberRecommendResponse> getMyRecommendsIntegrated(Authentication authentication,
      @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable) {
    return Response.success(
        memberService.getMyRecommendsIntegrated(authentication.getName(), pageable));
  }

  /**
   * 내가 등록한 comments와 다른 정보를 통합해서 불러옵니다.
   *
   * @return 작성 게시글 수, 팔로워 수, 팔로윙 수, 작성한 comments 목록
   */
  @GetMapping("/comments/integrated")
  public Response<MemberCommentResponse> getMyComments(Authentication authentication,
      @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable) {
    return Response.success(memberService.getMyComments(authentication.getName(), pageable));
  }
}
