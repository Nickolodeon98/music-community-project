package com.content_i_like.controller;

import com.content_i_like.domain.dto.TrackRequest;
import com.content_i_like.domain.dto.search.SearchRequest;
import com.content_i_like.domain.entity.Track;
import com.content_i_like.domain.enums.TrackEnum;
import com.content_i_like.service.TrackService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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