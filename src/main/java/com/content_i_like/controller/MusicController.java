package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.domain.dto.tracks.TrackInfoWithPrimaryKey;
import com.content_i_like.service.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/music")
@RequiredArgsConstructor
public class MusicController {

  private final MusicService musicService;

  @GetMapping("/track")
  public String showTrackInfo(@RequestParam(value="pk", required = false) Long trackPK,
      @RequestParam(value="page", required = false) Integer pageNum,
      Model model) {

    TrackGetResponse trackAndRecommends = musicService.getASingleTrackInfo(trackPK, "sjeon0730@gmail.com");

    model.addAttribute("trackInfo", trackAndRecommends);
    model.addAttribute("trackRecommendsAsList", trackAndRecommends.getRecommendsOfTracks());

    return "pages/search/tracks-details";
  }
}
