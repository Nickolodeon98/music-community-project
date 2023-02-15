package com.content_i_like.domain.dto.recommend;

import com.content_i_like.domain.entity.Comment;
import com.content_i_like.domain.entity.Recommend;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RecommendListResponse {

  private Long recommendNo;
  private String recommendTitle;
  private String recommendImageUrl;
  private String memberNickname;
  private String albumImageUrl;
  private String trackTitle;
  private String artistName;
  private String recommendContent;
  private Long countLikes;
  private Long accumulatedPoints;

  public static RecommendListResponse of(Recommend recommend) {

    return RecommendListResponse.builder()
        .recommendNo(recommend.getRecommendNo())
        .recommendTitle(recommend.getRecommendTitle())
        .recommendImageUrl(recommend.getRecommendImageUrl())
        .memberNickname(recommend.getMember().getNickName())
        .albumImageUrl(recommend.getTrack().getAlbum().getAlbumImageUrl())
        .trackTitle(recommend.getTrack().getTrackTitle())
        .artistName(recommend.getTrack().getAlbum().getArtist().getArtistName())
        .recommendContent(recommend.getRecommendContent())
        .countLikes((long) recommend.getLikes().size())
        .accumulatedPoints(recommend.getComments().stream()
            .mapToLong(Comment::getCommentPoint)
            .sum() + recommend.getRecommendPoint())
        .build();
  }

  public static List<RecommendListResponse> of(Page<Object[]> result) {
    return result.stream()
        .map(objects -> RecommendListResponse.builder()
            .recommendNo((Long) objects[0])
            .recommendTitle(limitStringLength((String) objects[1], 10))
            .recommendImageUrl((String) objects[2])
            .memberNickname((String) objects[3])
            .trackTitle(limitStringLength((String) objects[4], 16))
            .albumImageUrl((String) objects[5])
            .artistName(limitStringLength((String) objects[6], 13))
            .recommendContent(limitStringLength((String) objects[7], 50))
            .countLikes((Long) objects[8])
            .accumulatedPoints(objects[9] == null ? 0L : (Long) objects[9])
            .build())
        .collect(Collectors.toList());
  }

  private static String limitStringLength(String str, int length) {
    if (str.length() <= length) {
      return str;
    }
    return str.substring(0, length) + "...";
  }
}