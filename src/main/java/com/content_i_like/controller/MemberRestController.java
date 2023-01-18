package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.member.MemberJoinRequest;
import com.content_i_like.domain.dto.member.MemberJoinResponse;
import com.content_i_like.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberRestController {
    private final MemberService memberService;

    @PostMapping("/join")
    public Response<MemberJoinResponse> join(@RequestBody MemberJoinRequest request){
        MemberJoinResponse userJoinResponse = memberService.join(request);
        return Response.success(userJoinResponse);
    }

}
