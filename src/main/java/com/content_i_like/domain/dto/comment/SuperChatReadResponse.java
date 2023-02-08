package com.content_i_like.domain.dto.comment;

import com.content_i_like.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SuperChatReadResponse {
  private Long commentNo;
  private String memberNickname;
  private Long commentPoint;
  private String profileImgUrl;

  public static SuperChatReadResponse of(Comment comment) {
    return SuperChatReadResponse.builder()
        .commentNo(comment.getCommentNo())
        .memberNickname(comment.getMember().getNickName())
        .commentPoint(comment.getCommentPoint())
        .profileImgUrl(comment.getMember().getProfileImgUrl())
        .build();
  }
}
