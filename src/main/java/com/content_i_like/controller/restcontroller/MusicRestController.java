package com.content_i_like.controller.restcontroller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.service.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/music")
@RequiredArgsConstructor
public class MusicRestController {

  private final MusicService musicService;

  @GetMapping("/track/{trackNo}")
  public Response<TrackGetResponse> showTrackInfo(final Authentication authentication,
      @PageableDefault(sort="recommendTitle", direction= Direction.DESC) Pageable pageable,
      @PathVariable final Long trackNo) {
    TrackGetResponse track = musicService.getASingleTrackInfo(trackNo, pageable, authentication.getName());
    return Response.success(track);
  }


}
