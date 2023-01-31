package com.content_i_like.domain.dto.comment;

import com.content_i_like.domain.entity.Comment;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Recommend;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentRequest {

  private String commentContent;
  private Long commentPoint;

  public Comment toEntity(Member member, Recommend post) {
    return Comment.builder()
        .commentContent(this.commentContent)
        .commentPoint(this.commentPoint)
        .member(member)
        .recommend(post)
        .build();
  }

}
