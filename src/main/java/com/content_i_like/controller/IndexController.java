package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.recommend.RecommendListResponse;
import com.content_i_like.service.RecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping()
@RequiredArgsConstructor
public class IndexController {

  private final RecommendService recommendService;

  @GetMapping("/")
  public String index(Model model) {
    log.info("main index");
    Pageable pageable = PageRequest.of(0, 20, Sort.by("createdAt").ascending());

    Page<RecommendListResponse> response = recommendService.getPostList(pageable);
    model.addAttribute("response", response);

    return "index";
  }
}