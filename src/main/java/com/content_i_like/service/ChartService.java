package com.content_i_like.service;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.chart.RecommendChartResponse;
import com.content_i_like.domain.dto.chart.TrackChartResponse;
import com.content_i_like.repository.ChartQueryRepository;
import com.querydsl.core.Tuple;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChartService {

  private final ChartQueryRepository chartQueryRepository;

  @Transactional
  public List<RecommendChartResponse> getRecommendChart(String sort) throws Exception {
    if (sort.equals("monthly")) {
      return chartQueryRepository.getMonthlyRecommendChartTop10();
    }
    return chartQueryRepository.getWeeklyRecommendChartTop10();
  }

  @Transactional
  public List<TrackChartResponse> getTrackChart(String sort) throws Exception {
    if (sort.equals("monthly")) {
      return chartQueryRepository.getMonthlyTrackChartTop10();
    }
    return chartQueryRepository.getWeeklyTrackChartTop10();
  }
}
