package com.content_i_like.domain.dto.chart;

import com.querydsl.core.Tuple;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChartResponse {

  private List<Tuple> chart;
}
