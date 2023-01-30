package com.content_i_like.domain.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentDeleteResponse {

  private Long commentNo;
  private Long recommendNo;
  private String message;
}
