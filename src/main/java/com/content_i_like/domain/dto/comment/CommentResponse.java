package com.content_i_like.domain.dto.comment;

import com.content_i_like.domain.entity.Comment;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Recommend;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CommentResponse {

  private Long commentNo;
  private Long postNo;
  private String commentContent;
  private Long commentPoint;
  private String profileImgUrl;
  private String memberNickname;
  private LocalDateTime createdAt;

  public static CommentResponse of(Comment comment, Recommend post, Member member) {
    return CommentResponse.builder()
        .commentNo(comment.getCommentNo())
        .postNo(post.getRecommendNo())
        .commentContent(comment.getCommentContent())
        .commentPoint(comment.getCommentPoint())
        .profileImgUrl(member.getProfileImgUrl())
        .memberNickname(member.getNickName())
        .build();
  }
}
