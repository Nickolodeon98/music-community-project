package com.content_i_like.domain.dto.recommend;

import com.content_i_like.domain.entity.Recommend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RecommendPostResponse {

  private Long recommendNo;
  private String recommendTitle;
  private Long recommendPoint;

  public static RecommendPostResponse fromEntity(Recommend recommend) {
    return RecommendPostResponse.builder()
        .recommendNo(recommend.getRecommendNo())
        .recommendTitle(recommend.getRecommendTitle())
        .recommendPoint(recommend.getRecommendPoint())
        .build();
  }
}