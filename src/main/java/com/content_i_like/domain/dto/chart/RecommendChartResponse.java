package com.content_i_like.domain.dto.chart;

import com.querydsl.core.Tuple;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecommendChartResponse {

  private Long recommendNo;
  private String recommendTitle;
  private Long memberNo;
  private String memberNickName;
  private Long recommendScore;
  private Long recommendViews;

}
