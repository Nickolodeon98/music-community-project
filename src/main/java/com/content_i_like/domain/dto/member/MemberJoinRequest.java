package com.content_i_like.domain.dto.member;

import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.enums.GenderEnum;
import com.content_i_like.domain.enums.MemberStatusEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberJoinRequest {

//  @NotBlank(message = "이메일은 필수 입력 값입니다.")
//  @Email(message = "이메일 형식에 맞지 않습니다.")
  private String email;

//  @NotBlank(message = "닉네임은 필수 입력 값입니다.")
  private String nickName;

//  @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
//  @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
//      message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
  private String password;

//  @NotBlank(message = "이름은 필수 입력 값입니다.")
  private String name;

  private GenderEnum gender;
  private Integer birth;

  public Member toEntity(String password, String profileImgUrl) {
    return Member.builder()
        .email(this.email)
        .password(password)
        .gender(this.gender)
        .name(this.name)
        .profileImgUrl(profileImgUrl)
        .nickName(this.nickName)
        .birth(this.birth)
        .status(MemberStatusEnum.USER)
        .build();
  }
}
