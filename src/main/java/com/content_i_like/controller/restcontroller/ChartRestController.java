package com.content_i_like.controller.restcontroller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.chart.RecommendChartResponse;
import com.content_i_like.domain.dto.chart.TrackChartResponse;
import com.content_i_like.service.ChartService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chart")
@RequiredArgsConstructor
@Slf4j
public class ChartRestController {

  private final ChartService chartService;

  @GetMapping("/recommend")
  public Response<List<RecommendChartResponse>> getRecommendChart(
      @RequestParam(required = false, defaultValue = "monthly") String sort) throws Exception {
    List<RecommendChartResponse> findChart = chartService.getRecommendChart(sort);

    log.info("chart = {}", findChart);
    return Response.success(findChart);
  }

  @GetMapping("/track")
  public Response<List<TrackChartResponse>> getTrackChart(
      @RequestParam(required = false, defaultValue = "monthly") String sort) throws Exception {
    List<TrackChartResponse> findChart = chartService.getTrackChart(sort);

    log.info("chart = {}", findChart);
    return Response.success(findChart);
  }
}
