package com.content_i_like.domain.dto.comment;

import com.content_i_like.domain.entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CommentReadResponse {

  private Long commentNo;
  private String memberNickname;
  private String profileImgUrl;
  private String commentContent;
  private Long commentPoint;
  private Long memberNo;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
  private LocalDateTime createdAt;

  public static CommentReadResponse of(Comment comment) {
    Long commentPoint;
    if (comment.getCommentPoint() == null || comment.getCommentPoint() == 0) {
      commentPoint = null;
    }else{
      commentPoint = comment.getCommentPoint();
    }
    return CommentReadResponse.builder()
        .commentNo(comment.getCommentNo())
        .memberNickname(comment.getMember().getNickName())
        .profileImgUrl(comment.getMember().getProfileImgUrl())
        .commentContent(comment.getCommentContent())
        .commentPoint(commentPoint)
        .createdAt(comment.getCreatedAt())
        .memberNo(comment.getMember().getMemberNo())
        .build();
  }

  public static CommentReadResponse commentWithMaxPoints(CommentReadResponse a,
      CommentReadResponse b) {
    return a.getCommentPoint() >= b.getCommentPoint() ? a : b;
  }

}
