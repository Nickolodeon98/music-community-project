package com.content_i_like.controller;

import com.content_i_like.domain.dto.chart.RecommendChartResponse;
import com.content_i_like.service.ChartService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chart")
@RequiredArgsConstructor
@Slf4j
public class ChartController {

  private final ChartService chartService;

  @GetMapping("/recommend-monthly")
  public String getMonthlyRecommendChart(Model model) throws Exception {
    model.addAttribute("charts", chartService.getMonthlyRecommendChart());
    return "/pages/chart/recommend-chart";

  }

  @GetMapping("/recommend-weekly")
  public String getWeeklyRecommendChart(Model model) throws Exception {
    model.addAttribute("charts", chartService.getWeeklyRecommendChart());
    return "/pages/chart/recommend-chart";
  }
}
