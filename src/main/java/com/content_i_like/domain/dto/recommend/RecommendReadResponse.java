package com.content_i_like.domain.dto.recommend;

import com.content_i_like.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RecommendReadResponse {

  private String recommendTitle;
  private String memberNickname;
  private String albumImageUrl;
  private String trackTitle;
  private String artistName;
  private List<Comment> comments;
  private String recommendContent;
  private Long countLikes;
  private Long recommendPoint;
  private Long accumulatedPoints;
  private String recommendYoutubeUrl;
}
