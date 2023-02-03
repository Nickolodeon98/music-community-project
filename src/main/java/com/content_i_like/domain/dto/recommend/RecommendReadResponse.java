package com.content_i_like.domain.dto.recommend;

import com.content_i_like.domain.entity.Album;
import com.content_i_like.domain.entity.Artist;
import com.content_i_like.domain.entity.Comment;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Recommend;
import com.content_i_like.domain.entity.Track;
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

  public static RecommendReadResponse of(Recommend post, Member member, Album album, Artist artist,
      Track track, List<Comment> comments, Long countLikes, Long accumulatedPoints) {
    return RecommendReadResponse.builder()
        .recommendTitle(post.getRecommendTitle())
        .memberNickname(member.getNickName())
        .albumImageUrl(album.getAlbumImageUrl())
        .trackTitle(track.getTrackTitle())
        .artistName(artist.getArtistName())
        .comments(comments)
        .recommendContent(post.getRecommendContent())
        .countLikes(countLikes)
        .recommendPoint(post.getRecommendPoint())
        .accumulatedPoints(accumulatedPoints)
        .recommendYoutubeUrl(post.getRecommendYoutubeUrl())
        .build();
  }
}
