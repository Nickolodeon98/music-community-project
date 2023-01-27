package com.content_i_like.domain.dto.member;

import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Point;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberResponse {

  private String email;
  private String profileImgUrl;
  private String nickName;
  private String introduction;
  private String status;
  private String gender;
  private Integer birth;
  private List<Point> point;

  public MemberResponse toResponse(Member member) {
    return MemberResponse.builder()
        .email(member.getEmail())
        .profileImgUrl(member.getProfileImgUrl())
        .nickName(member.getNickName())
        .introduction(member.getIntroduction())
        .status(String.valueOf(member.getStatus()))
        .gender(String.valueOf(member.getGender()))
        .birth(member.getBirth())
        .point(member.getPointNo())
        .build();
  }
}
