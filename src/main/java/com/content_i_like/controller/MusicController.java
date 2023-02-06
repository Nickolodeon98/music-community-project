package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.service.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/music")
@RequiredArgsConstructor
public class MusicController {

  private final MusicService musicService;

  @GetMapping("/track/{trackNo}")
  public Response<TrackGetResponse> showTrackInfo(final Authentication authentication,
      @PathVariable final Long trackNo) {
    TrackGetResponse track = musicService.getASingleTrackInfo(trackNo, authentication.getName());
    return Response.success(track);
  }


}
