package com.content_i_like.controller;

import com.content_i_like.service.ChartService;
import com.content_i_like.service.RecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/chart")
@RequiredArgsConstructor
@Slf4j
public class ChartController {

  private final ChartService chartService;
  private final RecommendService recommendService;

  @GetMapping("/recommend")
  public String getRecommendChart(Model model,
      @RequestParam(defaultValue = "monthly", name = "sort") String sort) throws Exception {
//    recommendService.updateScoreIfNull();
    model.addAttribute("charts", chartService.getRecommendChart(sort));
    return "pages/chart/recommend-chart";
  }

  @GetMapping(value = {"", "/track"})
  public String getTrackChart(Model model,
      @RequestParam(defaultValue = "monthly", name = "sort") String sort) throws Exception {
//    recommendService.updateScoreIfNull();
    model.addAttribute("charts", chartService.getTrackChart(sort));
    return "pages/chart/track-chart";
  }

}
