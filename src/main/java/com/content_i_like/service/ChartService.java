package com.content_i_like.service;

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
  public List<Tuple> getMonthlyRecommendChart() throws Exception {
    return chartQueryRepository.getMonthlyRecommendChartTop10();
  }

  @Transactional
  public List<Tuple> getWeeklyRecommendChart() throws Exception {
    return chartQueryRepository.getWeeklyRecommendChartTop10();
  }

}
