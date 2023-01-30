package com.content_i_like.domain.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentResponse {

  private Long commentNo;
  private Long postNo;
  private String commentContent;
  private Long commentPoint;
}
