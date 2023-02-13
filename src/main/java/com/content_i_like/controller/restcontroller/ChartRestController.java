package com.content_i_like.controller.restcontroller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.chart.ChartResponse;
import com.content_i_like.domain.dto.comment.CommentReadResponse;
import com.content_i_like.repository.ChartQueryRepository;
import com.content_i_like.service.ChartService;
import com.querydsl.core.Tuple;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chart")
@RequiredArgsConstructor
@Slf4j
public class ChartRestController {

  private final ChartService chartService;

  @GetMapping("/recommend-monthly")
  public ChartResponse getMonthlyRecommendChart() throws Exception {
    return chartService.getMonthlyRecommendChart();
  }

  @GetMapping("/recommend-weekly")
  public ChartResponse getWeeklyRecommendChart() throws Exception {
    return chartService.getWeeklyRecommendChart();
  }
}
