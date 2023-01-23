package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.service.LikesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recommends")
@Slf4j
public class LikesRestController {

    private final LikesService likesService;

    @PostMapping("/{recommendNo}/likes")
    public Response<String> changeLikesStatus(final Authentication authentication,
                                              @PathVariable final Long recommendNo){
        log.info("authentication = {}, recommendNo = {}", authentication, recommendNo);

        String userName = authentication.getName();
        return Response.success(likesService.changeLikesStatus(userName, recommendNo));
    }
}
