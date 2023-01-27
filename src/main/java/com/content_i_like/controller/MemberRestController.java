package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.member.*;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Point;
import com.content_i_like.service.MailService;
import com.content_i_like.service.MemberService;
import com.content_i_like.service.PointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberRestController {

  private final MemberService memberService;
  private final MailService mailService;
  private final PointService pointService;

  @PostMapping("/join")
  public Response<MemberJoinResponse> join(@RequestBody @Valid final MemberJoinRequest request) {
    Member member = memberService.join(request);
    pointService.giveWelcomePoint(member);
    return Response.success(new MemberJoinResponse(member.getMemberNo(), member.getNickName()));
  }

  @PostMapping("/login")
  public Response<MemberLoginResponse> login(@RequestBody @Valid final MemberLoginRequest request) {
    MemberLoginResponse response = memberService.login(request);
    return Response.success(response);
  }

  @PostMapping("/passwd/find_pw")
  public Response<String> findPw(@RequestBody @Valid final MemberFindRequest request) {
    MailDto mailDto = memberService.findPwByEmail(request);
    System.out.println("메일을 발송하겠습니다.");
    mailService.mailSend(mailDto);
    return Response.success("임시 비밀번호를 메일로 발송했습니다.");
  }

  @PostMapping("/passwd/change")
  public Response<String> changePw(@RequestBody @Valid final ChangePwRequest request,
      final Authentication authentication) {
    String username = authentication.getName();
    return Response.success(memberService.changePw(request, username));
  }

  @GetMapping("/my")
  public Response<MemberResponse> getMyInfo(final Authentication authentication) {
    String username = authentication.getName();
    MemberResponse memberResponse = memberService.getMyInfo(username);
    return Response.success(memberResponse);
  }

  @PutMapping("/my")
  public Response<MemberResponse> modifyMyInfo(
      @RequestPart(value = "dto") @Valid final MemberModifyRequest request,
      @RequestPart(value = "file", required = false) MultipartFile file,
      final Authentication authentication) throws IOException {
    String username = authentication.getName();
    MemberResponse memberResponse = memberService.modifyMyInfo(request, file, username);
    return Response.success(memberResponse);
  }

  @PutMapping("/my/profileImg")
  public Response<String> updateProfileImg(@RequestPart(value = "file") MultipartFile file,
      Authentication authentication) throws IOException {
    String username = authentication.getName();
    String url = memberService.uploadProfileImg(username, file);
    return Response.success(url);
  }

}
