package com.content_i_like.controller;

import com.content_i_like.domain.dto.tracks.TrackRequest;
import com.content_i_like.domain.entity.Track;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {

  @GetMapping("/thymeleaf")
  public String testForThymeleafListModel(@ModelAttribute("trackRequestDto") TrackRequest trackRequest) {
    log.info("로그입니다");
    Track track = Track.builder().trackTitle(trackRequest.getTrackTitle()).build();
    log.info("title:{}",trackRequest.getTrackTitle());
    return "pages/search/tracks-search";
  }
}