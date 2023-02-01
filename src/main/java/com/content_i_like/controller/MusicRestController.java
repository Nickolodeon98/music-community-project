package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.service.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/music")
@RequiredArgsConstructor
public class MusicRestController {

  private final MusicService musicService;

  @GetMapping("/all")
  public Response<Page<TrackGetResponse>> showAllTracks(final Authentication authentication,
      @PageableDefault(sort = "trackNo", direction = Sort.Direction.DESC) Pageable pageable) {

    Page<TrackGetResponse> tracks = musicService.getEveryTrack(pageable, authentication.getName());

    return Response.success(tracks);
  }

}
