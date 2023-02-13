package com.content_i_like.service;

import com.content_i_like.domain.dto.chart.ChartResponse;
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
  public ChartResponse getMonthlyRecommendChart() throws Exception {
    return new ChartResponse(chartQueryRepository.getMonthlyRecommendChartTop10());
  }

  @Transactional
  public ChartResponse getWeeklyRecommendChart() throws Exception {
    return new ChartResponse(chartQueryRepository.getWeeklyRecommendChartTop10());
  }

}
