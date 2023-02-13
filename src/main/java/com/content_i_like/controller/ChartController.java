package com.content_i_like.controller;

import com.content_i_like.service.ChartService;
import com.querydsl.core.Tuple;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chart")
@RequiredArgsConstructor
@Slf4j
public class ChartController {

  private final ChartService chartService;

  @GetMapping("/recommend-monthly")
  public List<Tuple> getMonthlyRecommendChart() throws Exception {
    return chartService.getMonthlyRecommendChart();
  }

  @GetMapping("/recommend-weekly")
  public List<Tuple> getWeeklyRecommendChart() throws Exception {
    return chartService.getWeeklyRecommendChart();
  }
}
