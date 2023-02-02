package com.content_i_like.domain.dto.search;

import com.content_i_like.domain.dto.recommend.RecommendListResponse;
import com.content_i_like.domain.entity.Comment;
import com.content_i_like.domain.entity.Recommend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SearchRecommendsResponse {
  private String recommendTitle;
  private String summarizedRecommendContent;
  private String memberNickname;
  private String recommendImageUrl;
  private String albumImageUrl;
  private Long countLikes;
  private Long accumulatedPoints;
  private String createdAt;

  public static SearchRecommendsResponse of(Recommend recommend) {
    return SearchRecommendsResponse.builder()
        .recommendTitle(recommend.getRecommendTitle())
        .memberNickname(recommend.getMember().getNickName())
        .recommendImageUrl(recommend.getRecommendImageUrl())
        .albumImageUrl(recommend.getTrack().getAlbum().getAlbumImageUrl())
        .countLikes((long) recommend.getLikes().size())
        .accumulatedPoints(recommend.getComments().stream()
            .mapToLong(Comment::getCommentPoint)
            .sum() + recommend.getRecommendPoint())
        .createdAt(recommend.getCreatedAt().toString())
        .build();
  }
}
