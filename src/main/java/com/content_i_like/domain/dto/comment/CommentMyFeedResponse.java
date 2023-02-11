package com.content_i_like.domain.dto.comment;

import com.content_i_like.domain.entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CommentMyFeedResponse {

  private Long commentNo;
  private String memberNickname;
  private String profileImgUrl;
  private String commentContent;
  private Long commentPoint;
  private String recommendTitle;
  private Long fewDaysAgo;
  private String trackInfo;
  private Long recommendNo;
  private String recommendImgUrl;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
  private LocalDateTime createdAt;

  public static CommentMyFeedResponse of(Comment comment) {
    return CommentMyFeedResponse.builder()
        .commentNo(comment.getCommentNo())
        .memberNickname(comment.getMember().getNickName())
        .profileImgUrl(comment.getMember().getProfileImgUrl())
        .commentContent(comment.getCommentContent())
        .commentPoint(comment.getCommentPoint())
        .recommendTitle(comment.getRecommend().getRecommendTitle())
        .fewDaysAgo(
            (long) Period.between(comment.getCreatedAt().toLocalDate(), LocalDate.now()).getDays())
        .createdAt(comment.getCreatedAt())
        .trackInfo(comment.getRecommend().getTrack().getTrackTitle() + " - "+ comment.getRecommend().getTrack().getArtist().getArtistName())
        .recommendNo(comment.getRecommend().getRecommendNo())
        .recommendImgUrl(comment.getRecommend().getRecommendImageUrl())
        .build();
  }


}
