package com.content_i_like.domain.dto.recommend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecommendModifyRequest {

  private String recommendTitle;
  private String recommendContent;
  private String recommendImageUrl;
  private String recommendYoutubeUrl;
}
