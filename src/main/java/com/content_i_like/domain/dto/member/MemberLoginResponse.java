package com.content_i_like.domain.dto.member;

import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.enums.GenderEnum;
import com.content_i_like.domain.enums.MemberStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Getter
public class MemberLoginResponse {

  private String jwt;
  private Long memberNo;
  private String email;
  private String nickName;
  private String profileImgUrl;
  private String name;
  private String snsCheck;
  private String introduction;
  private MemberStatusEnum status;
  private GenderEnum gender;
  private Integer birth;

  public static MemberLoginResponse toResponse(Member member, String token){
    return MemberLoginResponse.builder()
        .jwt(token)
        .memberNo(member.getMemberNo())
        .email(member.getEmail())
        .nickName(member.getNickName())
        .profileImgUrl(member.getProfileImgUrl())
        .name(member.getName())
        .snsCheck(member.getSnsCheck())
        .introduction(member.getIntroduction())
        .status(member.getStatus())
        .gender(member.getGender())
        .birth(member.getBirth())
        .build();
  }
}
