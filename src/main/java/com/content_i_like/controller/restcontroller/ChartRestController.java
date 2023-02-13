package com.content_i_like.controller.restcontroller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.chart.RecommendChartResponse;
import com.content_i_like.service.ChartService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chart")
@RequiredArgsConstructor
@Slf4j
public class ChartRestController {

  private final ChartService chartService;

  @GetMapping("/recommend-monthly")
  public Response<List<RecommendChartResponse>> getMonthlyRecommendChart() throws Exception {
    List<RecommendChartResponse> findChart = chartService.getMonthlyRecommendChart();

    log.info("chart = {}", findChart);
    return Response.success(findChart);
  }

  @GetMapping("/recommend-weekly")
  public Response<List<RecommendChartResponse>> getWeeklyRecommendChart() throws Exception {
    return Response.success(chartService.getWeeklyRecommendChart());
  }
}
