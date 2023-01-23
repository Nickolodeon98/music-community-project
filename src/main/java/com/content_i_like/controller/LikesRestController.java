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

    @PostMapping("/{recommendNo}/likes")
    public Response<String> changeLikesStatus(final Authentication authentication,
                                              @PathVariable final Long recommendNo) {
        log.info("authentication = {}, recommendNo = {}", authentication, recommendNo);

        String userName = authentication.getName();
        return Response.success(likesService.changeLikesStatus(userName, recommendNo));
    }

    @GetMapping("/{recommendNo}/likes")
    public Response<Integer> returnNumberLikes(@PathVariable final Long recommendNo) {
        return Response.success(likesService.countNumberLikes(recommendNo));
    }
}
