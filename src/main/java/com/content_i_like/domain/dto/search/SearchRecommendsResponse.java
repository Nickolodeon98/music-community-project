package com.content_i_like.domain.dto.search;

import com.content_i_like.domain.dto.comment.CommentReadResponse;
import com.content_i_like.domain.dto.recommend.RecommendListResponse;
import com.content_i_like.domain.entity.Comment;
import com.content_i_like.domain.entity.Recommend;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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
  private Long recommendNo;
  private String recommendTitle;
  private String summarizedRecommendContent;
  private String memberNickname;
  private String recommendImageUrl;
  private String albumImageUrl;
  private Long countLikes;
  private Long accumulatedPoints;
  private LocalDateTime createdAt;
  private String memberProfileImgUrl;
  private List<CommentReadResponse> comments;

  public static SearchRecommendsResponse of(Recommend recommend, String summarizedContent) throws NullPointerException {
    return SearchRecommendsResponse.builder()
        .recommendNo(recommend.getRecommendNo())
        .recommendTitle(recommend.getRecommendTitle())
        .summarizedRecommendContent(summarizedContent)
        .memberNickname(recommend.getMember().getNickName())
        .recommendImageUrl(recommend.getRecommendImageUrl())
        .albumImageUrl(recommend.getTrack().getAlbum().getAlbumImageUrl())
        .countLikes((long) recommend.getLikes().size())
        .createdAt(recommend.getCreatedAt())
        .memberProfileImgUrl(recommend.getMember().getProfileImgUrl())
        .comments(recommend.getComments().stream().map(CommentReadResponse::of).collect(Collectors.toList()))
//        .accumulatedPoints(recommend.getComments().stream()
//            .mapToLong(Comment::getCommentPoint)
//            .sum() + recommend.getRecommendPoint())
        .build();
  }
}
