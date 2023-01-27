package com.content_i_like.domain.dto.member;

import com.content_i_like.domain.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberModifyRequest {

  private String introduction;
  private GenderEnum gender;
  private Integer birth;
  private String newPassword;
  private String verification;
}
