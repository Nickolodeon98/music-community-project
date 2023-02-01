package com.content_i_like.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum PointTypeEnum {
  ATTENDANCE_CHECK("출석 보상"),
  RECOMMEND_POSTS("추천 글"),
  COMMENTS("댓글"),
  WELCOME_POINT("회원가입 보상"),
  TEST("테스트용");

  private String message;
}
