package com.content_i_like.domain.dto.chart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TrackChartResponse {
  private Long trackNo;
  private String trackTitle;
  private Long albumNo;
  private String albumImageUrl;
  private Long artistNo;
  private String artistName;
  private Long trackScore;
}
