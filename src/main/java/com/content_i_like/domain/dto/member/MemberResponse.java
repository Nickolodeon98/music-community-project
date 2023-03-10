package com.content_i_like.domain.dto.member;

import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Point;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberResponse {

  private String email;
  private String profileImgUrl;
  private String nickName;
  private String name;
  private String introduction;
  private String status;
  private String gender;
  private Integer birth;
  private Long point;

  public static MemberResponse responseWithPoint(Member member, Long point) {
    return MemberResponse.builder()
        .email(member.getEmail())
        .profileImgUrl(member.getProfileImgUrl())
        .nickName(member.getNickName())
        .name(member.getName())
        .introduction(member.getIntroduction())
        .status(String.valueOf(member.getStatus()))
        .gender(String.valueOf(member.getGender()))
        .birth(member.getBirth())
        .point(point)
        .build();
  }
}
