package com.content_i_like.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum NotificationType {
  POINT_ADJUSTMENT("포인트가 정산되었습니다"),
  POINT_WELCOME("회원가입을 축하드리며 소정의 포인트를 발급해드렸습니다."),
  LIKES("좋아요가 눌렸습니다."),
  COMMENTS("댓글이 달렸습니다.");

  private String text;
}
