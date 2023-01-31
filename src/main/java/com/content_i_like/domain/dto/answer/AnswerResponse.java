package com.content_i_like.domain.dto.answer;

import com.content_i_like.domain.entity.Answer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AnswerResponse {

  private Long answerNo;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
  private LocalDateTime createdAt;

  private String content;

  public static AnswerResponse of(Answer answer, LocalDateTime createdAt) {
    return AnswerResponse.builder()
        .answerNo(answer.getAnswerNo())
        .createdAt(createdAt)
        .content(answer.getAnswerContent())
        .build();
  }
}
