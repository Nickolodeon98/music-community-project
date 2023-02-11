package com.content_i_like.domain.dto.recommend;

import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Recommend;
import com.content_i_like.domain.entity.Track;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecommendPostRequest {

  private String recommendTitle;
  private String recommendContent;
  private String recommendYoutubeUrl;
  private Long recommendPoint;
  private Long trackNo;
  private MultipartFile image;
  private String hashtag;

  public static Recommend toEntity(RecommendPostRequest request, Member member, Track track,
      String url) {
    Long point;

    if (request.getRecommendPoint() == null) {
      point = 0L;
    }else{
      point = request.getRecommendPoint();
    }
    String youtubeUrl = "";

    if (request.getRecommendYoutubeUrl() != null) {
      youtubeUrl = "https://youtu.be/" + request.getRecommendYoutubeUrl();
    }

    return Recommend.builder()
        .recommendTitle(request.getRecommendTitle())
        .recommendContent(request.getRecommendContent())
        .recommendImageUrl(url)
        .recommendYoutubeUrl(youtubeUrl)
        .recommendPoint(point)
        .recommendViews(0L)
        .member(member)
        .track(track)
        .build();
  }
}
